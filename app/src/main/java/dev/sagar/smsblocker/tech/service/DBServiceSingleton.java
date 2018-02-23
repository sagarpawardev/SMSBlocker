package dev.sagar.smsblocker.tech.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dev.sagar.smsblocker.tech.beans.Contact;
import dev.sagar.smsblocker.tech.service.helper.SMSLocalContract.SMSLocal;
import dev.sagar.smsblocker.tech.service.helper.SMSLocalDBHelper;
import dev.sagar.smsblocker.tech.utils.LogUtil;

/**
 * Created by sagarpawar on 07/02/18.
 */

public class DBServiceSingleton {

    //Log Initiate
    private LogUtil log = new LogUtil( this.getClass().getName() );

    //Java Core
    private static DBServiceSingleton instance = null;
    private DBServiceSingleton(){}

    //Constants
    private final String _id = Telephony.Sms._ID;
    private final String threadId = Telephony.Sms.THREAD_ID;
    private final String address = Telephony.Sms.ADDRESS;
    private final String person = Telephony.Sms.PERSON;
    private final String date = Telephony.Sms.DATE;
    private final String protocol = Telephony.Sms.PROTOCOL;
    private final String read = Telephony.Sms.READ;
    private final String status = Telephony.Sms.STATUS;
    private final String type = Telephony.Sms.TYPE;
    private final String reply_path_present = Telephony.Sms.TYPE;
    private final String subject = Telephony.Sms.SUBJECT;
    private final String body = Telephony.Sms.BODY;
    private final String serviceCenter = Telephony.Sms.SERVICE_CENTER;
    private final String subscriptionId = Telephony.Sms.SUBSCRIPTION_ID;
    private final String locked = Telephony.Sms.LOCKED;
    private final String errorCode = Telephony.Sms.ERROR_CODE;
    private final String photoUri = ContactsContract.CommonDataKinds.Phone.PHOTO_URI;
    private final String photoUriThumbnail = ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI;
    private final String contactName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    private final String _id_local = SMSLocal.COLUMN_NAME_ID;
    private final String threadId_local = SMSLocal.COLUMN_NAME_THREAD_ID;
    private final String address_local = SMSLocal.COLUMN_NAME_ADDRESS;
    private final String person_local = SMSLocal.COLUMN_NAME_PERSON;
    private final String date_local = SMSLocal.COLUMN_NAME_DATE;
    private final String protocol_local = SMSLocal.COLUMN_NAME_PROTOCOL;
    private final String read_local = SMSLocal.COLUMN_NAME_READ;
    private final String status_local = SMSLocal.COLUMN_NAME_STATUS;
    private final String type_local = SMSLocal.COLUMN_NAME_TYPE;
    private final String reply_path_present_local = SMSLocal.COLUMN_NAME_TYPE;
    private final String subject_local = SMSLocal.COLUMN_NAME_SUBJECT;
    private final String body_local = SMSLocal.COLUMN_NAME_BODY;
    private final String serviceCenter_local = SMSLocal.COLUMN_NAME_SERVICE_CENTER;
    private final String subscriptionId_local = SMSLocal.COLUMN_NAME_SUBSCRIPTION_ID;
    private final String locked_local = SMSLocal.COLUMN_NAME_LOCKED;
    private final String errorCode_local = SMSLocal.COLUMN_NAME_ERROR_CODE;
    private final String photoUri_local = SMSLocal.COLUMN_NAME_PHOTO_URI;
    private final String photoUriThumbnail_local = SMSLocal.COLUMN_NAME_PHOTO_THUMBNAIL;
    private final String contactName_local = SMSLocal.COLUMN_NAME_CONTACT_NAME;
    private final String unreadCount_local = SMSLocal.COLUMN_NAME_UNREAD_COUNT;


    public static DBServiceSingleton getInstance(){
        if(instance == null)
            instance = new DBServiceSingleton();
        return instance;
    }

    /***
     * Provides Database service related to query
     * @param mContentResolver Content Resolver of context
     * @param mUri URI of Table
     * @param mProjection Query Projections
     * @param mSelection Query Selections
     * @param mSelectionArgs Query Selection Arguments
     * @param mSortOrder Query Sorting Order
     * @return Cursor for Query
     */
    public Cursor query(ContentResolver mContentResolver, Uri mUri, String[] mProjection, String mSelection, String[] mSelectionArgs, String mSortOrder){
        String methodName ="query()";
        log.justEntered(methodName);

        Cursor mCursor = mContentResolver.query(mUri, mProjection, mSelection, mSelectionArgs, mSortOrder);

        log.returning(methodName);
        return mCursor;
    }

    /***
     * Provides Database service related to insert
     * @param context Context
     * @param mUri URI of Table
     * @param mContentValues Query Content Values
     * @return URI of inserted row
     */
    public Uri insert(Context context, Uri mUri, ContentValues mContentValues){
        String methodName ="insert()";
        log.justEntered(methodName);

        //TODO Need to Check with conversation Data as well
        ContentResolver mContentResolver = context.getContentResolver();
        Uri uri = mContentResolver.insert(mUri, mContentValues);

        log.returning(methodName);
        return uri;
    }

    /***
     * Providers Database service for deleting
     * @param context Context
     * @param uri URI of Table
     * @param selection Query Selection values
     * @param selectionArgs Query Selection arguments
     * @return Count of deleted rows
     */
    public int delete(Context context, Uri uri, String selection, String[] selectionArgs){
        String methodName ="delete()";
        log.justEntered(methodName);

        //TODO need to delete from conversation Database as well and update from SMS database in case only latest SMS is deleted

        ContentResolver contentResolver = context.getContentResolver();
        int count = contentResolver.delete(uri, selection, selectionArgs);

        log.returning(methodName);
        return count;
    }

    /***
     * Provides Database Service for deleting
     * @param context Context
     * @param uri URI of Table
     * @param where Where Condition for update
     * @param selectionArgs Query Selection arguments
     * @param values Values to update
     * @return number of rows updated
     */
    public int update(Context context, Uri uri, ContentValues values, String where, String[] selectionArgs){
        String methodName ="update()";
        log.justEntered(methodName);

        log.info(methodName, "Updating SMS Database...");
        ContentResolver contentResolver = context.getContentResolver();
        int count = contentResolver.update(uri, values, where, selectionArgs);
        log.info(methodName, "SMS DB Updated with count: "+count);

        log.info(methodName, "Updating Conversation Database...");
        SMSLocalDBHelper dbHelper = new SMSLocalDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(values.containsKey(read)){ //Converting fields of different column names to Local Database column name
            String value = values.getAsBoolean(read) ? "1" : "0";
            values.remove(read);
            values.put(read_local, value);
            values.put(unreadCount_local, 0);
        }

        if(values.containsKey(date)){ //Converting fields of different column names to Local Database column name
            String value = values.getAsString(date);
            values.remove(date);
            values.put(date_local, value);
        }

        count = db.update(SMSLocal.TABLE_NAME, values, where, selectionArgs);
        log.info(methodName, "Conversation DB Updated count: "+count);
        db.close();
        dbHelper.close();

        log.returning(methodName);
        return count;
    }


    /***
     * Refreshes Conversation Shadow Database
     * @param context Context
     * @return Returns number of rows present in ShadowConversation
     */
    public int refreshConversations(Context context){
        String methodName = "refreshConversation()";
        log.justEntered(methodName);

        int result = 0;

        SMSLocalDBHelper mDBHelper = new SMSLocalDBHelper(context);

        //Content MAP
        ContentResolver contentResolver = context.getContentResolver();
        Uri mUriSMS = Telephony.Sms.CONTENT_URI;


        //Read Old Conversation DB Rows Starts
        log.info(methodName, "Reading old Address and Date rows");
        String[] mOldProjection = {
                this.address_local,
                this.date_local
        };
        SQLiteDatabase readDB = mDBHelper.getReadableDatabase();
        Cursor mOldCursor = readDB.query(SMSLocal.TABLE_NAME,
                mOldProjection, null, null, null, null, null);
        HashMap<String, Long> oldMap = new HashMap<>();
        if(mOldCursor!=null){
            log.info(methodName, "Reading Address and Date Count:"+mOldCursor.getCount());
            while(mOldCursor.moveToNext()){
                String address = mOldCursor.getString(mOldCursor.getColumnIndexOrThrow(this.address_local));
                /*String normalizeAddress = PhoneNumberUtils.normalizeNumber(originalAddress);
                String address = normalizeAddress;
                if(address.length() < 9){ //If Number is like VK-Voda its normalized code is 3856778
                    address = originalAddress;
                }*/

                log.info(methodName, "Reading contact details: "+address);
                long date = mOldCursor.getLong(mOldCursor.getColumnIndexOrThrow(this.date_local));
                oldMap.put(address, date);
            }
            mOldCursor.close();
        }
        else{
            log.error(methodName, "OLD Conversation Query returned null cursor");
        }
        //-- Read Old Conversation DB Rows Ends

        //Reading Latest SMS DB Rows from ContentProvider Starts
        log.info(methodName, "Reading Latest SMS from Contact");
        String[] mProjection = new String[]{"*"};
        String mSelection = "";
        String[] mSelectionArgs = {};
        String mSortOrder = this.date +" DESC";

        HashMap<String, Long> newMap = new HashMap<>();
        HashMap<String, Integer> unreadCountMap = new HashMap<>();

        SQLiteDatabase  writableDB = mDBHelper.getWritableDatabase();

        Cursor mLatestSmsCursor = contentResolver
                .query(mUriSMS, mProjection, mSelection, mSelectionArgs, mSortOrder);
        if (mLatestSmsCursor != null) {
            log.info(methodName, "Reading latest sms from contact count:"+mLatestSmsCursor.getCount());
            while(mLatestSmsCursor.moveToNext()){

                String id = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndexOrThrow(this._id));
                String address = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndexOrThrow(this.address));
                String body = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndexOrThrow(this.body));
                String subscriptionId = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndexOrThrow(this.subscriptionId));
                String read = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.read));
                long date = mLatestSmsCursor.getLong(mLatestSmsCursor.getColumnIndexOrThrow(this.date));
                long type = mLatestSmsCursor.getLong(mLatestSmsCursor.getColumnIndexOrThrow(this.type));

                String threadId = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.threadId));
                String person = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.person));
                String protocol = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.protocol));
                String status = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.status));
                String reply_path_present = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.reply_path_present));
                String subject = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.subject));
                String locked = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.locked));
                String errorCode = mLatestSmsCursor.getString(mLatestSmsCursor.getColumnIndex(this.errorCode));

                /*String normalizedAddress = PhoneNumberUtils.normalizeNumber(address);
                String address = normalizedAddress;
                if(address.length() < 8){ //If Number is like VK-Voda its normalized code is 3856778
                    address = originalAddress;
                }*/

                log.info(methodName, "Reading SMS from contactNumber: "+address+" ");


                ContentValues values = new ContentValues();
                values.put(this._id_local, id);
                values.put(this.address_local, address);
                values.put(this.body_local, body);
                values.put(this.subscriptionId_local, subscriptionId);
                values.put(this.read_local, read);
                values.put(this.date_local, date);
                values.put(this.type_local, type);
                values.put(this.threadId_local, threadId);
                values.put(this.person_local, person);
                values.put(this.protocol_local, protocol);
                values.put(this.status_local, status);
                values.put(this.reply_path_present_local, reply_path_present);
                values.put(this.subject_local, subject);
                values.put(this.locked_local, locked);
                values.put(this.errorCode_local, errorCode);

                //If unread increment count
                log.info(methodName, "Raw Unread value: "+read+" for address: "+address);
                boolean bUnread = (read.equals("0") || read.equalsIgnoreCase("false")); //If object is saved as integer or boolean
                if(bUnread){
                    Integer unreadCount = unreadCountMap.get(address);
                    int count = unreadCount==null ? 0 : unreadCount;
                    count++;
                    unreadCountMap.put(address, count); //Increment count here
                    values.put(this.unreadCount_local, count);
                    log.info(methodName, "Adding unread count: "+count+" for address: "+address);
                }


                if(newMap.containsKey(address)) continue; //If value is already in Map then go to next value
                long oldDate = oldMap.get(address)==null ? 0 : oldMap.get(address);
                if(oldDate >= date) continue; //If oldDate is greater than or equals current date ignore message

                if(oldMap.containsKey(address)) {
                    String whereClause = this.address_local + " = ?";
                    String[] whereArgs = {address};
                    writableDB.update(SMSLocal.TABLE_NAME, values, whereClause, whereArgs);
                }
                else{
                    writableDB.insert(SMSLocal.TABLE_NAME, null, values);
                }
                newMap.put(address, date);
                result++;
            }
            mLatestSmsCursor.close();
        }
        else{
            log.error(methodName, "SMS Query returned null cursor");
        }
        //-- Reading Latest SMS DB Rows from ContentProvider Ends


        //Create a map of all contacts Starts
        Uri mContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] mContactProjection = {
                this.photoUri,
                this.photoUriThumbnail,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                this.contactName
        };

        Map<String, ContactDetails> mContactMap = new HashMap<>();
        Cursor mContactCursor = contentResolver.query(mContactUri, mContactProjection, null, null, null);
        if (mContactCursor != null) {

            while(mContactCursor.moveToNext()){
                String contactName = mContactCursor.getString(mContactCursor.getColumnIndex(this.contactName));
                String photoThumb = mContactCursor.getString(mContactCursor.getColumnIndex(this.photoUriThumbnail));
                String photoUri = mContactCursor.getString(mContactCursor.getColumnIndex(this.photoUri));
                String address = mContactCursor.getString(mContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                /*String normalizedAddress = PhoneNumberUtils.normalizeNumber(address);*/

                ContactDetails contact = new ContactDetails();
                contact.name = contactName;
                contact.photoThumbUri = photoThumb;
                contact.photoUri = photoUri;
                contact.address = address;

                mContactMap.put(address, contact);

                //TODO Default country to format number

                log.info(methodName, "Found contactName: "+contactName+" for address: "+address+" In new map: "+newMap.containsKey(address));
            }
            mContactCursor.close();
        }
        else{
            log.error(methodName, "Nothing in Contacts Cursor for "+address);
        }
        //-- Create a map of all contacts Ends

        //Update Photo in Database Starts
        Set<String> keySet = newMap.keySet();
        for(String key: keySet){
            ContactDetails contact = mContactMap.get(key);

            if(contact == null){ //In case address does not exists in Contacts
                continue;
            }

            ContentValues values = new ContentValues();
            values.put(this.contactName_local, contact.name);
            values.put(this.photoUri_local, contact.photoUri);
            values.put(this.photoUriThumbnail_local, contact.photoThumbUri);

            String whereClause = this.address_local + " = ?";
            String[] whereArgs = {contact.address};
            writableDB.update(SMSLocal.TABLE_NAME, values, whereClause, whereArgs);
        }
        //-- Update Photo in Database Ends

        //TODO: Delete Condition when conversation is removed from main database but exists in Local Database

        writableDB.close();
        mDBHelper.close();
        log.returning(methodName);
        return result;
    }


    class ContactDetails{
        String name, photoUri, photoThumbUri, address;
    }
}