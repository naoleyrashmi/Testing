
set STACK_NAME="mystack"
set TEMPLATE="template.yaml"
set PARAMETERS_FILE="params.json"
set PARAMS=($(jq -r '.Parameters[] | [.ParameterKey, .ParameterValue] | "\(.[0])=\(.[1])"' ${PARAMETERS_FILE}))

aws cloudformation deploy --template-file "${TEMPLATE}" --stack-name "${STACK_NAME}" --parameter-overrides ${PARAMS[@]}