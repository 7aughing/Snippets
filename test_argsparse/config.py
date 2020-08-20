# coding:utf-8

class Config(object):
    # if charset == "nums":
    #     pass
    # else charset == "alphabets":
    #     labels = ['_','zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nignt']
    
    labels = ['_','zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nignt']
    id2chr = {0: '_', 1: 'zero', 2: 'one', 3: 'two', 4: 'three', 5: 'four', 6: 'five', 7: 'six', 8: 'seven', 9: 'eight', 10: 'nignt'}
    chr2id = dict([v, k] for k, v in id2chr.items() )
    
    decode_type = "Greedy"
    
#     train_path = "/home/ld-sgdev/tiantian_duan/workspace/lipreading_from_lmks/data/train/"
#     val_path = "/home/ld-sgdev/tiantian_duan/workspace/lipreading_from_lmks/data/val/"

    train_path = "/home/ld-sgdev/tiantian_duan/workspace/lipreading_from_lmks/data/dists_train/"
    val_path = "/home/ld-sgdev/tiantian_duan/workspace/lipreading_from_lmks/data/dists_val/"
    
    beta1 = 0.9
    beta2 = 0.999
     
    epochs = 150
    lr = 0.0001
    lr_decay_iters = 30
    weight_decay = 0.000
    
    USE_CUDA = True
    num_gpu = 1
    batch_size = 4 * num_gpu
    rnn_dropout = 0.0
    
    save_iters = 5
    print_iters = 100    # 50 steps print
    ckp_save_dir = "./ckps/"
    log_save_dir = "./logs/"
    
    def _parse(self, kwargs):
        """ update the params of cfg according to the dict kwargs """
        for k, v in kwargs.items():
            if not hasattr(self, k):
                warnings.warn(' Warning: opt has not attribute %s'% k)
            setattr(self, k, v)
            
opt = Config