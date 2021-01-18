#matrixVectorMulti.py
#"to run" syntax example: mpiexec -n 3 python matrixVectorMulti.py
from mpi4py import MPI
import numpy as np
import math
import sys

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

#to enable/disable detail logging
debug = 0

#initialize as numpy arrays
dot = np.array([0.])

#initialize a matrix
#prepare an arbitrary matrix A of [size x size]
A = []
B = []
val = 0.0

for row in range(size):
	B.append([])
	for col in range(size):
                val = val + 1
                B[row].append(val)

if (rank == 0):
        A = np.array(B)
#A = np.array([[1.,2.,3.],[4.,5.,6.],[7.,8.,9.]]) if comm.rank == 0 else None

if (debug == 1):
        print "Matrix B", B
        print "Matrix A", A

vec = np.linspace(1, size, size)

if (debug == 1):
        print "vec=", vec
comm.Bcast(vec, root=0)

local_a = np.zeros(size)
comm.Scatter(A, local_a)

#local computation of dot product
local_dot = np.array([np.dot(local_a, vec)])

#gather data in single matrix
gather_a = np.zeros(size)
comm.Gather(local_dot, gather_a)

if rank == 0:
        print "Output Matrix", gather_a


