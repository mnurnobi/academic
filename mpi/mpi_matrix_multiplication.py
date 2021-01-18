#matrixMultiDynamic.py
#"to run" syntax example: mpiexec -n 3 python matrixMultiDynamic.py
from mpi4py import MPI
import numpy as np
from numpy.linalg import norm
import math
import sys

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

#to enable/disable detail logging
debug = 0

#read from command line
#n = int(sys.argv[1])    #length of vectors

#initialize as numpy arrays
dot = np.array([0.])

#initialize a matrix
A = np.array([[1.,2.,3.],[4.,5.,6.],[7.,8.,9.]]) if comm.rank == 0 else None
if (debug == 1):
        print "Matrix A", A

#vec = np.linspace(1, 3, 3)
vec = np.array([2,1,3])
if (debug == 1):
        print "vec=", vec
comm.Bcast(vec, root=0)

local_a = np.zeros(3)
comm.Scatter(A, local_a)

#local computation of dot product
local_dot = np.array([np.dot(local_a, vec)])

#gather data in single matrix
gather_a = np.zeros(3)
comm.Gather(local_dot, gather_a)
if rank == 0:
        print "Gather", gather_a

'''debug
#sum the results of each
comm.Reduce(local_dot, dot, op = MPI.SUM)
print "local dot", local_dot
print "process ", rank, "has ", local_a

if rank == 0:
        print "total sum", dot[0]
'''



