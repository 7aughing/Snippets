"""
    This is a example for test multiprocessing + tqdm
"""

import multiprocessing
import tqdm
import glob
import time

def show_name(idx):
    print(idx)
    time.sleep(0.5)

if __name__ == '__main__':
    img_paths = glob.glob("/home/ld-sgdev/tiantian_duan/workspace/lipreading_from_2D/data/mth_lmks/train/"+"*.npy")

    with multiprocessing.Pool(8) as pool:
        r = list(tqdm.tqdm(pool.imap(show_name, range(len(img_paths))), total=len(img_paths)))

    print('done')