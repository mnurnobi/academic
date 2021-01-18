#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>

pthread_mutex_t mtx;
pthread_cond_t conVariable;
int nthreads;
int count;

void *PhilosopherThread(void *threadid);
void creatPhilosopher(int nthreads);
void thinking(int threadIndex);
void pickUpChopsticks(int threadIndex);
void eating(int threadIndex);
void putDownChopsticks(int threadIndex);
void initializeMutex();



int main(int argc, char *argv[])
{
   nthreads = atoi(argv[1]);
   char *name = "Mohammad Nadim";
   count = 0;
   //printf ("You are %s and you created %d threads.\n" ,name,nthreads);
   initializeMutex();
   creatPhilosopher(nthreads);
   
}

void initializeMutex(){
   int i;
   for (i=0; i<nthreads; i++){
      pthread_mutex_init(&mtx, NULL);
   }
}

void *PhilosopherThread(void *threadIndex)
{
   long tid;
   tid = (long)threadIndex;
   //printf("This is philosopher %ld.\n", tid+1);


   thinking(tid);
   pickUpChopsticks(tid);
   eating(tid);
   putDownChopsticks(tid);

   pthread_exit(NULL);
}

void creatPhilosopher(int nthreads){
   pthread_t threads[nthreads];
   int rc;
   long t;
   for(t=0;t<nthreads;t++){
     rc = pthread_create(&threads[t], NULL, PhilosopherThread, (void *)t);
     }

   int rcc;
   long tt;
   for(tt=0;tt<nthreads;tt++){
     rcc = pthread_join(threads[tt], NULL);
     }

   //printf("%ld threads have been joined successfully now.\n", tt);
}

void thinking (int threadIndex){
   //printf("Philosopher %d is thinking.\n", threadIndex+1);
   usleep(rand()%500); 
}

void pickUpChopsticks (int threadIndex){
   
   pthread_mutex_lock(&mtx);
   while(threadIndex != count){
      pthread_cond_wait(&conVariable, &mtx);
   }
}

void eating (int threadIndex){
   printf("Philosopher %d is eating.\n", threadIndex+1); 
   usleep(rand()%500);  
}

void putDownChopsticks (int threadIndex){
   count++;
   pthread_mutex_unlock(&mtx);
   pthread_cond_broadcast(&conVariable);
   printf("Philosopher %d put down the chopstick.\n", threadIndex+1); 
}


