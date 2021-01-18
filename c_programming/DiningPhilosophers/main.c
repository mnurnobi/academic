//
//  main.c
//  DiningPhilosophers
//
//  Created by Nur Nobi on 10/20/18.
//  Copyright © 2018 Nur Nobi. All rights reserved.
//

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <time.h>

#define CUSTOMSLEEP() (usleep(rand()%500)

#define RANDNUM(min, max) \
((rand() % (int)(((max) + 1) - (min))) + (min))

void createPhilosophers(int nThreads);
void * PhilosopherThread(void * idx);

void thinking(int threadIndex);
void pickUpChopsticks(int threadIndex);
void eating(int threadIndex);
void putDownChopsticks(int threadIndex);

/* Declaration of the global variable.*/
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t conditionalVariable = PTHREAD_COND_INITIALIZER;
int count;

int main(int argc, const char * argv[]) {
    
    if (argc != 2)
        printf("Wrong Input, Number of argument is- %d \n", argc);
    
    int nthreads = 5;
    //int nthreads = atoi(argv[1]);
    count = 1;
    
    int i;
    for (i=1; i<=nthreads; i++){
        pthread_mutex_init(&mutex, NULL);
    }
    
    createPhilosophers(nthreads);
    
    return 0;
}

void createPhilosophers(int nThreads)
{
    pthread_t thread_id[nThreads];
    int rc;
    
    for(int i=1; i <= nThreads; ++i)
    {
        rc = pthread_create(&thread_id[1], NULL, PhilosopherThread, (void *) i);
        if(rc)
        {
            printf("\n Thread Creation ERROR: code is %d\n", rc);
            exit(1);
        }
    }
    
    for(int j=1; j <= nThreads; ++j)
    {
        pthread_join (thread_id[j], NULL);
    }
    
    //printf("%d threads have been joined successfully now.\n", nThreads);
    
}

void * PhilosopherThread(void * idx) {
    
    int threadIndex = (int)idx;
    
    //printf("This is philosopher %d\n", threadIndex);
    
    pthread_mutex_lock(&mutex);
    while(threadIndex != count){
        pthread_cond_wait(&conditionalVariable, &mutex);
    }
    
    thinking(threadIndex);
    pickUpChopsticks(threadIndex);
    eating(threadIndex);
    putDownChopsticks(threadIndex);
    
    pthread_exit(NULL);
}

void thinking(int threadIndex)
{
    CUSTOMSLEEP());
}

void pickUpChopsticks(int threadIndex)
{
    //printf("Philosopher %d picked up the chopstick.\n", threadIndex);
}

void eating(int threadIndex)
{
    printf("Philosopher %dth is eating\n", threadIndex);
    CUSTOMSLEEP());
}

void putDownChopsticks(int threadIndex)
{
    count++;
    pthread_cond_broadcast(&conditionalVariable);
    pthread_mutex_unlock(&mutex);
    
    printf("Philosopher %d put down the chopstick.\n", threadIndex);
}
