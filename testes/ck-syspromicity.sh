#!/bin/bash
echo "Analysing versions of syspromocity"
echo "Analysing v1 of syspromocity"
export MYCOMMIT=383b3e3466dd075909d2e0079b5722ba833c913d && export MYVERSION=v1 && . ck.sh
echo "Analysing v2 of syspromocity"
export MYCOMMIT=b8c3a7ea7be6b8c88407ab75a25b86163ad04abd && export MYVERSION=v2 && . ck.sh
echo "Analysing v3 of syspromocity"
export MYCOMMIT=fc0998d367aea7219a4791fcbcc5155b125b456e && export MYVERSION=v3 && . ck.sh
echo "Analysing v4 of syspromocity"
export MYCOMMIT=c58cc0e6a8bb9355745b4fd5cc7accf3a56b4668 && export MYVERSION=v4 && . ck.sh