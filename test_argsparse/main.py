import argparse
from config import opt


parser = argparse.ArgumentParser(description="test args parse")


parser.add_argument('--train_root', type=str, help="train dir")
parser.add_argument('--val_root', type=str, help="val dir")
parser.add_argument('--lr', type=float, help="learning rate")

args = parser.parse_args()

opt.train_path = args.train_root
opt.val_path = args.val_root
opt.learning_rate = args.lr 


if __name__ == "__main__":
    print(opt.train_path)
    print(opt.val_path)
    print(opt.learning_rate)
