import os
from google.cloud import storage

os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'ServiceKey_GoogleCloud.json'

storage_client = storage.Client()

# Create a new bucket
bucket_name = 'b21-cap0391-bucket'
bucket = storage_client.bucket(bucket_name)

# Upload a file to bucket function
def upload_to_bucket(blob_name, file_path, bucket_name):
    try:
        # Get bucket
        bucket = storage_client.get_bucket(bucket_name)

        blob = bucket.blob(blob_name)
        blob.upload_from_filename(file_path)
        return True

    except Exception as e:
        print(e)
        return False

# Download a file from bucket function
def download_from_bucket(blob_name, file_path, bucket_name):
    try:
        # Get bucket
        bucket = storage_client.get_bucket(bucket_name)

        blob = bucket.blob(blob_name)
        with open(file_path, 'wb') as f:
            storage_client.download_blob_to_file(blob, f)
        return True
    
    except Exception as e:
        print(e)
        return False


file_path = r'C:\Users\Au\Downloads'
upload_to_bucket('image.PNG', os.path.join(file_path, 'image.PNG'), bucket_name)


download_from_bucket('image.PNG', os.path.join(os.getcwd(), 'image.PNG'), bucket_name)
