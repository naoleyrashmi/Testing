#!/bin/bash
#check only one parameter is passed to the script
noOfParameters=$#
if [ $noOfParameters != 1 ]; then
  echo "invalid no of arguments passed to the script"
  echo "usage ./paramsValidator.sh 'ACCOUNT_NAME,<comma seperated list of parameters>' "
  exit 1
fi

parameters=$1
errorArray=()

for i in $(echo $parameters | sed "s/,/ /g")
do
  if [ "${!i}" == "" ]; then
    errorArray+=("${i} is not valid")
  fi
done

if [ ${#errorArray[@]} -eq 0 ]; then
  echo "Variables are valid"
  exit 0
else
  echo "######################################################"
  for i in "${errorArray[@]}"
  do
    echo ${i}
  done
  echo "######################################################"
  exit 1
fi
