#!/bin/sh

echo "Copying Command component to Rental-Command"
cp rental/src/main/java/io/axoniq/demo/bikerental/rental/command/*.java microservices/rental-command/src/main/java/io/axoniq/demo/bikerental/microservices/rentalcommand

echo "Copying Payment Saga component to Rental-Payment"
cp rental/src/main/java/io/axoniq/demo/bikerental/rental/paymentsaga/*.java microservices/rental-payment/src/main/java/io/axoniq/demo/bikerental/microservices/rentalpayment

echo "Copying Query component to Rental-Query"
cp rental/src/main/java/io/axoniq/demo/bikerental/rental/query/*.java microservices/rental-query/src/main/java/io/axoniq/demo/bikerental/microservices/rentalquery

echo "Copying UI component to Rental-UI"
cp rental/src/main/java/io/axoniq/demo/bikerental/rental/ui/*.java microservices/rental-ui/src/main/java/io/axoniq/demo/bikerental/microservices/rentalui

echo "Finished microservices creation process"
