#vectorSupremNorm.py
#"to run" syntax example: mpiexec -n 4 python vectorSupremNorm.py 40000
from mpi4py import MPI
import numpy as np
from numpy.linalg import norm
import math
import sys

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

#read from command line
n = int(sys.argv[1])    #length of vectors

#arbitrary example vectors, generated to be evenly divided by the
#number of processes for convenience

x = np.linspace(0, 300, n) if comm.rank == 0 else None

#initialize as numpy arrays
dot = np.array([0.])

#lists to prepare Send and displacement tuples
sendList = []
disList = []

#test for conformability
if rank == 0:
         if (n != x.size):
                  print("vector length mismatch")
                  comm.Abort()
         #length of each process's portion of the original vector
         print("n = %s, size = %s" % (n, size))

#prepare dynamic localn and tuples for scatterv
quotient = n/size
reminder = n%size

for index in range(size-1):
        sendList.append(quotient)
        disList.append(index*quotient)

#adding reminder parts of the N with quotient to get total equal length 22/4 = 5, extL = 7
extLen = quotient+reminder

sendList.append(extLen)
disList.append((index+1)*quotient)

sendTuple = tuple(sendList)
disTuple = tuple(disList)

#initialize as dynamic numpy arrays
#assign extLen to local_x of processor of rank = size-1 (last processor)
if (rank == size-1 and reminder > 0):
        local_x = np.zeros(extLen)
else:
        local_x = np.zeros(quotient)

#divide up dynamic vectors
comm.Scatterv([x, sendTuple, disTuple, MPI.DOUBLE], local_x)

#local computation of dot product
max = np.max(local_x)

#find overall all MAX
comm.Reduce(max, dot, op = MPI.MAX)
if rank == 0:
        print "max By reduce", dot[0]

