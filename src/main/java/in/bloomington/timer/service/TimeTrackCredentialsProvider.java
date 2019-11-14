package in.bloomington.timer.service;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.annotations.SdkPublicApi;

@SdkPublicApi
public class TimeTrackCredentialsProvider implements AwsCredentials{

		String access_key_id = "";
		String secret_access_key = "";

		public TimeTrackCredentialsProvider(){

		}

		public TimeTrackCredentialsProvider(String val, String val2){

				setAccessKeyId(val);
				setSecretAccessKey(val2);

		}
		void setAccessKeyId(String val){
				if(val != null)
						access_key_id = val;
		}
		void setSecretAccessKey(String val){
				if(val != null)
						secret_access_key = val;
		}
		/**
     * Retrieve the AWS access key, used to identify the user interacting with AWS.
     */
    public String accessKeyId(){
				return access_key_id;
		}

    /**
     * Retrieve the AWS secret access key, used to authenticate the user interacting with AWS.
     */
    public String secretAccessKey(){
				return secret_access_key;
		}


}
		
		
