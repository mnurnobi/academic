cd bin

rmic mybooks.com.shared.BookSellerMain
rmic mybooks.com.shared.TimeSyncOperator
rmic mybooks.com.shared.BookSellerSub

java mybooks.com.shared.WorkerServer server3 &

cd ../


