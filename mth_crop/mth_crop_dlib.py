# coding:utf-8
import os
import glob
import sys
import math
import time
import random
from multiprocessing import Pool

import cv2
import dlib
import numpy as np
from pyexiv2 import Image
import argparse


parser = argparse.ArgumentParser(description="feature selection")
parser.add_argument('--feature_type', type=str, help="feature type ")


detector = dlib.get_frontal_face_detector()
predictor_path = "./models/shape_predictor_68_face_landmarks.dat"
predictor = dlib.shape_predictor(predictor_path)
input_dir = "./data/"
out_dir1 = "./results/frames/"
out_dir2 = "./results/lmks/"


args = parser.parse_args()


def get_rotate_info(image_path):
    """
    Args:
        image_path: image path in disk
    """
    frame = Image(image_path)
    res1 = frame.read_exif()
    res2 = frame.read_iptc()
    res3 = frame.read_xmp()

    print(res1)
    print('*'*30)
    print(res2)
    print('*'*30)
    print(res3)
    

def draw_lmks(frame, lmks):
    """
    Args:
        frame: face image
        lmks: face landmarks
    Return: 
        show face with face landmarks
    """
    for idx, point in enumerate(lmks):
        pos = (point[0], point[1])

        cv2.circle(frame, pos, 3, color=(0, 255, 0))
        cv2.putText(frame, str(idx+1), pos, cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 0, 255), 1, cv2.LINE_AA)
    
    cv2.imshow("frame", frame)
    cv2.waitKey(0)


def align(frame, lmks):
    leyes = lmks[36:42]
    reyes = lmks[42:48]

    leye_center = np.mean(leyes, axis=0).astype("int")
    reye_center = np.mean(reyes, axis=0).astype("int")

    dx = reye_center[0] - leye_center[0]
    dy = reye_center[1] - leye_center[1]

    angle = math.atan2(dy, dx) * 180 / math.pi

    eye_center = ((leye_center[0]+reye_center[0]) // 2, (leye_center[1] + reye_center[1]) // 2)

    rotate_matrix = cv2.getRotationMatrix2D(eye_center, angle, scale=1)
    rotated_frame = cv2.warpAffine(frame, rotate_matrix, (frame.shape[1], frame.shape[0]))    

    return rotated_frame, eye_center, angle


def rotate_point(origin, point, angle, row):
    x1, y1 = point
    x2, y2 = origin
    y1 = row - y1
    y2 = row - y2

    angle = math.radians(angle)
    x = x2 + math.cos(angle) * (x1 - x2) - math.sin(angle) * (y1 - y2)
    y = y2 + math.sin(angle) * (x1 - x2) + math.cos(angle) * (y1 - y2)
    y = row - y

    return int(x), int(y)


def rotate_lmks(lmks, eye_center, angle, row):
    rotated_lmks = list()
    for i in range(len(lmks)):
        rotated_lmk = rotate_point(origin=eye_center, point=lmks[i], angle=angle, row=row)
        rotated_lmks.append(rotated_lmk)
    return rotated_lmks


def crop_mth(frame, lmks):
    x4 = lmks[4][0]
    y8 = lmks[8][1]
    x12 = lmks[12][0]
    y51 = lmks[51][1]
    r = int(max((x12 - x4) / 2, (y51 - y8) / 2))
    xAve, yAve = 0, 0
    for lmk_idx in range(48, 68):
        xAve += lmks[lmk_idx][0]
        yAve += lmks[lmk_idx][1]
    
    # frame_lmk = np.array(lmks[48: 68]).reshape(-1, 40)  # x1,y1, x2,y2
    # np.save("xx.npy", frame_lmk)

    xAve, yAve = int(xAve / 20), int(yAve / 20)
    mth_frame = frame[yAve - r: yAve + r, xAve - r: xAve + r]

    # draw_lmks(frame, lmks[48: 68])
    return mth_frame


def process_vid(vid_path):
    mth_frames = []

    device_id, vid_name = vid_path.strip().split("/")[-2:]
    if args.feature_type == "mth_imgs":
        out_dir = out_dir1+device_id
    elif args.feature_type == "mth_lmks":
        out_dir = out_dir2+device_id

    idx = 0

    if not os.path.exists(out_dir):
        os.makedirs(out_dir)

    cap = cv2.VideoCapture(vid_path)  # 获取视频或者摄像头输入流

    while True:
        # 每次循环处理一帧
        ret, img = cap.read()
        if img is None:
            break
        idx += 1
        process_frame_to_mth(img, out_dir, idx)

    cap.release()
    return mth_frames


def process_frame_to_mth(frame, out_dir, idx):
    faces = detector(frame, 1)
    if len(faces) != 1:
        print("[err]: multi faces! ")
    else:
        face = faces[0]

        # step0: face localization
        shape = predictor(frame, face)
        lmks = np.array([[p.x, p.y] for p in shape.parts()])

        # step1: face alignment
        rotated_frame, eye_center, angle = align(frame, lmks)

        # step2: lmks rotation
        rotated_lmks = rotate_lmks(lmks, eye_center, angle, frame.shape[0])
        # draw_lmks(rotated_frame, rotated_lmks)
        
        if args.feature_type == "mth_img":
            mth_img = crop_mth(rotated_frame, rotated_lmks)
            cv2.imwrite(out_dir+"/"+str(idx).zfill(4)+".jpg", mth_img)
        elif args.feature_type == "mth_lmks":
            tmp = np.array(rotated_lmks)
            np.save(out_dir+"/"+str(idx).zfill(4)+".npy", tmp)


def main():
    vids = glob.glob(input_dir+"/*/*.mp4")
    pool = Pool(5)
    for vid_path in vids:
        pool.apply_async(process_vid, args=(vid_path, ))
    
    pool.close()
    pool.join()


if __name__ == "__main__":
    main()