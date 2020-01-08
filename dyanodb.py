import boto3

db = boto3.client('dynamodb')
# table = db.Table('qwde-DynamoDB-1HZ4E4B84JUMV')
# print(table)
response = db.put_item(
       TableName='testdb',
       Item= {
        'testid': '123',
        'username': 'test',
        'first_name': 'admin',
        'last_name': 'admin123',
        'age': 30,
        'account_type': 'administrator',
    }
)
# print(Item)