> paxos算法中,proposer, acceptor, learner分别需要记录哪些信息?


### Proposer 记录的信息：
- 提案编号（proposal number）：用于唯一标识提案，防止冲突。
- 提案值（proposal value）：要提交的提案内容。
- 已经接受该提案的 acceptor 的数量（accepted count）：用于计算是否已经得到了大多数 acceptor 的接受。
### Acceptor 记录的信息：
- 最高提案编号（highest proposal number）：已经接受过的最高提案编号。
- 最大被接受的提案（highest accepted proposal）：已经接受过的最大提案。
- 最后一次接受的提案（last accepted proposal）：最后一次接受的提案的提案编号和提案值。
### Learner 记录的信息：
- 已经接受该提案的 acceptor 的数量（accepted count）：用于计算是否已经得到了大多数 acceptor 的接受。
- 已经接受的提案值（accepted proposal value）：已经接受的提案的值。