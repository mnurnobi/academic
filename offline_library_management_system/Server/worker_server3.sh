cd bin

rmic mybooks.com.shared.BookSellerMain
rmic mybooks.com.shared.BookSellerSub
rmic mybooks.com.shared.TimeSyncOperator

java mybooks.com.shared.WorkerServer server1 &

cd ../


