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

#arbitrary example vectors
x = np.linspace(0, 100, n) if comm.rank == 0 else None

if rank == 0:
        mxnorm_x = norm(x, np.inf)
        print "mxnorm-x", mxnorm_x
