import os
import requests
import pickle
import numpy as np
from google.cloud import storage
from google.cloud import logging

os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'ServiceKey_GoogleCloud.json'
bucket_name = 'b21-cap0391-bucket'
logger_name = 'c0040310'

# Upload a file to bucket function
def upload_to_bucket(blob_name, file_path, bucket_name):
    # Instantiates a client
    storage_client = storage.Client()
    bucket = storage_client.bucket(bucket_name)
    try:
        # Get bucket
        bucket = storage_client.get_bucket(bucket_name)

        blob = bucket.blob(blob_name)
        blob.upload_from_filename(file_path)
        
        print("File {} uploaded to {}".format(blob_name, bucket_name))
        return True

    except Exception as e:
        write_log_entry(str(e))
        print(e)
        return False

file_path = r'C:\Users\Au\Downloads'
upload_to_bucket('input_img.PNG', os.path.join(file_path, 'image.PNG'), bucket_name)

# Rename object in bucket
def rename_blob(blob_name, new_name):

    storage_client = storage.Client()
    bucket = storage_client.bucket(bucket_name)
    blob = bucket.blob(blob_name)

    new_blob = bucket.rename_blob(blob, new_name)

    print("Blob {} has been renamed to {}".format(blob.name, new_blob.name))

rename_blob('input_img.PNG', 'output_img.PNG')

# Download a file from bucket function
def download_from_bucket(blob_name, file_path, bucket_name):
    # Instantiates a client
    storage_client = storage.Client()
    bucket = storage_client.bucket(bucket_name)
    try:
        # Get bucket
        bucket = storage_client.get_bucket(bucket_name)

        blob = bucket.blob(blob_name)
        with open(file_path, 'wb') as f:
            storage_client.download_blob_to_file(blob, f)

        print("File {} downloaded from {}".format(blob_name, bucket_name))
        return True
    
    except Exception as e:
        write_log_entry(str(e))
        print(e)
        return False

download_from_bucket('output_img.PNG', os.path.join(os.getcwd(), 'output_img.PNG'), bucket_name)

# Write Log
def write_log_entry(log_text):
    # Instantiates a client
    logging_client = logging.Client()
    logging_client.get_default_handler()
    logging_client.setup_logging()

    logger = logging_client.logger(logger_name)
    logger.log_text(log_text, severity="ERROR")




