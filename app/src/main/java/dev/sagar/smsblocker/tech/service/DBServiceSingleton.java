package dev.sagar.smsblocker.tech.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dev.sagar.smsblocker.Permission;
import dev.sagar.smsblocker.tech.beans.SMS;
import dev.sagar.smsblocker.tech.service.helper.conversation.ConversationDBAttributes;
import dev.sagar.smsblocker.tech.service.helper.conversation.ConversationDBAttributes.Converesation;
import dev.sagar.smsblocker.tech.service.helper.DBHelper;
import dev.sagar.smsblocker.tech.service.helper.savedsms.SavedSMSDBAttributes;
import dev.sagar.smsblocker.tech.utils.LogUtil;
import dev.sagar.smsblocker.tech.utils.PermissionUtilSingleton;
import dev.sagar.smsblocker.tech.utils.PhoneUtilsSingleton;

/**
 * Created by sagarpawar on 07/02/18.
 */

public class DBServiceSingleton {

    //Log Initiate
    private LogUtil log = new LogUtil( this.getClass().getName() );

    //Java Core
    private static DBServiceSingleton instance = null;
    private static PermissionUtilSingleton permUtil = null;
    private static PhoneUtilsSingleton phoneUtils = PhoneUtilsSingleton.getInstance();
    private DBServiceSingleton(){}

    //<editor-fold desc="Java Constants">
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
    private final String contactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

    private final String _id_local = Converesation.COLUMN_NAME_ID;
    private final String threadId_local = Converesation.COLUMN_NAME_THREAD_ID;
    private final String address_local = Converesation.COLUMN_NAME_ADDRESS;
    private final String person_local = Converesation.COLUMN_NAME_PERSON;
    private final String date_local = Converesation.COLUMN_NAME_DATE;
    private final String protocol_local = Converesation.COLUMN_NAME_PROTOCOL;
    private final String read_local = Converesation.COLUMN_NAME_READ;
    private final String status_local = Converesation.COLUMN_NAME_STATUS;
    private final String type_local = Converesation.COLUMN_NAME_TYPE;
    private final String reply_path_present_local = Converesation.COLUMN_NAME_TYPE;
    private final String subject_local = Converesation.COLUMN_NAME_SUBJECT;
    private final String body_local = Converesation.COLUMN_NAME_BODY;
    private final String serviceCenter_local = Converesation.COLUMN_NAME_SERVICE_CENTER;
    private final String subscriptionId_local = Converesation.COLUMN_NAME_SUBSCRIPTION_ID;
    private final String locked_local = Converesation.COLUMN_NAME_LOCKED;
    private final String errorCode_local = Converesation.COLUMN_NAME_ERROR_CODE;
    private final String photoUri_local = Converesation.COLUMN_NAME_PHOTO_URI;
    private final String photoUriThumbnail_local = Converesation.COLUMN_NAME_PHOTO_THUMBNAIL;
    private final String contactName_local = Converesation.COLUMN_NAME_CONTACT_NAME;
    private final String unreadCount_local = Converesation.COLUMN_NAME_UNREAD_COUNT;

    public static final String TABLE_STARSMS = SavedSMSDBAttributes.SavedSMS.TABLE_NAME;
    //</editor-fold>


    public static DBServiceSingleton getInstance(){
        if(instance == null) {
            instance = new DBServiceSingleton();
            permUtil = PermissionUtilSingleton.getInstance();
        }
        return instance;
    }

    /***
     * Provides Database service related to query
     * @param tableName Name of table
     * @param mProjection Query Projections
     * @param mSelection Query Selections
     * @param mSelectionArgs Query Selection Arguments
     * @param mSortOrder Query Sorting Order
     * @return Cursor for Query
     */
    public Cursor query(SQLiteDatabase db, String tableName, String[] mProjection, String mSelection, String[] mSelectionArgs, String mSortOrder){
        String methodName ="query()";
        log.justEntered(methodName);

        Cursor mCursor = db.query(tableName, mProjection, mSelection, mSelectionArgs, null, null, mSortOrder);

        log.returning(methodName);
        return mCursor;
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
     * Provides Database service related to insert
     * @param context Context
     * @param tableName Table to insert
     * @param mContentValues Query Content Values
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public boolean insert(Context context, String tableName, ContentValues mContentValues){
        String methodName ="insert()";
        log.justEntered(methodName);

        boolean result = false;
        SQLiteOpenHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(tableName, null, mContentValues);
        db.close();
        dbHelper.close();
        log.debug(methodName, "Got Received row_id: "+rowId);
        if(rowId != -1)
            result = true;

        log.returning(methodName);
        return result;
    }

    /***
     * Providers Database service for deleting
     * @param context Context
     * @param tableName name of the table
     * @param whereClause Where Clause parameter
     * @param whereArgs Where Clause Arguments
     * @return Count of deleted rows
     */
    public int delete(Context context, String tableName, String whereClause, String[] whereArgs){
        String methodName ="delete(Context, String, String, String[])";
        log.justEntered(methodName);

        SQLiteOpenHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(tableName, whereClause, whereArgs);
        db.close();
        dbHelper.close();

        log.returning(methodName);
        return count;
    }

    /**
     * Proved Delete Service in data base
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public int delete(Context context, Uri uri, String selection, String[] selectionArgs){
        String methodName ="delete(Context , Uri , String , String[] )";
        log.justEntered(methodName);

        ContentResolver contentResolver = context.getContentResolver();
        int count = contentResolver.delete(uri, selection, selectionArgs);

        //TODO need to delete from conversation Database as well and update from SMS database in case only latest SMS is deleted


        //if deleting thread then Delete Thread from Conversation DB
        try {
            Uri deleteThreadUri = Telephony.Sms.CONTENT_URI;
            String colThreadId = Telephony.Sms.THREAD_ID;
            String threadLikeDeleteQuery = colThreadId + "LIKE?"; // Removed all spaces just for comparision
            String threadEqualDeleteQuery = colThreadId + "=?";
            String formattedSelection = selection.replace(" ", "");

            if (count > 0 && uri.equals(deleteThreadUri) &&
                    (formattedSelection.equalsIgnoreCase(threadLikeDeleteQuery) ||
                            formattedSelection.equalsIgnoreCase(threadEqualDeleteQuery)
                    )) {
                String convColThreadId = Converesation.COLUMN_NAME_THREAD_ID;
                DBHelper dbHelper = new DBHelper(context);
                SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
                String convSelection = selection.replaceFirst(colThreadId, convColThreadId);
                int rowCount = writableDB.delete(DBConstants.TABLE_CONVERSATION, convSelection, selectionArgs);
                log.info(methodName, "Deleted Thread from Conversation count: " + rowCount);
                writableDB.close();;
                dbHelper.close();
            }
        }catch (Exception ex){
            //This is added in case Some null pointer is occurred
            log.error(methodName, "Some Exception Occurred while playing with delete_thread_id from conversation: "+ex.getMessage());
        }

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
        DBHelper dbHelper = new DBHelper(context);
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

        count = db.update(Converesation.TABLE_NAME, values, where, selectionArgs);
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

        DBHelper mDBHelper = new DBHelper(context);

        //Content MAP
        ContentResolver contentResolver = context.getContentResolver();
        Uri mUriSMS = Telephony.Sms.CONTENT_URI;


        //<editor-fold desc="Read Old Conversation DB Rows Starts">
        //Read Old Conversation DB Rows Starts
        log.info(methodName, "Reading old Address and Date rows");
        String[] mOldProjection = {"*"};
        SQLiteDatabase readDB = mDBHelper.getReadableDatabase();
        Cursor mOldCursor = readDB.query(Converesation.TABLE_NAME,
                mOldProjection, null, null, null, null, null);
        HashMap<String, SMS> oldMap = new HashMap<>();
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
                String id = mOldCursor.getString(mOldCursor.getColumnIndexOrThrow(this._id_local));
                String body = mOldCursor.getString(mOldCursor.getColumnIndexOrThrow(this.body_local));
                int subscriptionId = mOldCursor.getInt(mOldCursor.getColumnIndexOrThrow(this.subscriptionId_local));
                String read = mOldCursor.getString(mOldCursor.getColumnIndex(this.read_local));
                boolean bRead = (read.equals("0") || read.equalsIgnoreCase("false"));
                long type = mOldCursor.getLong(mOldCursor.getColumnIndexOrThrow(this.type_local));
                String threadId = mOldCursor.getString(mOldCursor.getColumnIndex(this.threadId_local));

                SMS sms = new SMS();
                sms.setId(id);
                sms.setAddress(address);
                sms.setDateTime(date);
                sms.setBody(body);
                sms.setSubscription(subscriptionId);
                sms.setRead(bRead);
                sms.setType(type);
                sms.setThreadId(threadId);

                oldMap.put(threadId, sms);
            }
            mOldCursor.close();
        }
        else{
            log.error(methodName, "OLD Conversation Query returned null cursor");
        }
        //-- Read Old Conversation DB Rows Ends
        //</editor-fold>

        //Reading Latest SMS DB Rows from ContentProvider Starts
        log.info(methodName, "Reading Latest SMS from Contact");
        String[] mProjection = new String[]{"*"};
        String mSelection = "";
        String[] mSelectionArgs = {};
        String mSortOrder = this.date +" DESC";

        HashSet<String> doneSet = new HashSet<>();
        HashSet<String> addressDoneSet = new HashSet<>(); //Adding temporarily
        HashMap<String, Integer> unreadCountMap = new HashMap<>();

        SQLiteDatabase  writableDB = mDBHelper.getWritableDatabase();

        Cursor mLatestSmsCursor = contentResolver
                .query(mUriSMS, mProjection, mSelection, mSelectionArgs, mSortOrder);

        if (mLatestSmsCursor != null) {
            log.info(methodName, "Reading latest sms from contact count:"+mLatestSmsCursor.getCount());
            while(mLatestSmsCursor.moveToNext()){

                //<editor-fold desc="SMS Field init">
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
                //</editor-fold>


                //<editor-fold desc="Increment Unread Count">
                log.info(methodName, "Raw Unread value: "+read+" for thread_id: "+threadId);
                boolean bUnread = (read.equals("0") || read.equalsIgnoreCase("false")); //If object is saved as integer or boolean
                if(bUnread){
                    Integer unreadCount = unreadCountMap.get(threadId);
                    int count = unreadCount==null ? 0 : unreadCount;
                    count++;
                    unreadCountMap.put(threadId, count); //Increment count here
                    values.put(this.unreadCount_local, count);
                    log.info(methodName, "Adding unread count: "+count+" for thread_id: "+threadId);
                }
                //</editor-fold>


                if(doneSet.contains(threadId)) continue; //If value is already in Map then go to next value

                SMS oldSMS = oldMap.get(threadId);
                if(oldSMS!=null && oldSMS.getId().equals(id)){ //If New message the oldSMS will be null
                    oldMap.remove(threadId);
                    doneSet.add(threadId);
                    String formattedAddress = phoneUtils.formatNumber(context, address);
                    addressDoneSet.add(formattedAddress);
                    continue; //If SMS is same as older ones
                }

                log.error(methodName, "");
                if(oldMap.containsKey(threadId)) {
                    String whereClause = this.threadId_local + " LIKE ?";
                    String[] whereArgs = {threadId};
                    writableDB.update(Converesation.TABLE_NAME, values, whereClause, whereArgs);
                }
                else{
                    writableDB.insert(Converesation.TABLE_NAME, null, values);
                }

                //Move Thread id from old_bucket to done_bucket
                oldMap.remove(threadId);
                doneSet.add(threadId);
                String formattedAddress = phoneUtils.formatNumber(context, address);
                addressDoneSet.add(formattedAddress);

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
                this.contactName,
                this.contactId
        };

        Map<String, ContactDetails> mContactMap = new HashMap<>();

        if(permUtil.hasPermission(context, Permission.READ_CONTACTS)) {
            Cursor mContactCursor = contentResolver.query(mContactUri, mContactProjection, null, null, null);
            if (mContactCursor != null) {

                while (mContactCursor.moveToNext()) {
                    String contactName = mContactCursor.getString(mContactCursor.getColumnIndex(this.contactName));
                    String photoThumb = mContactCursor.getString(mContactCursor.getColumnIndex(this.photoUriThumbnail));
                    String photoUri = mContactCursor.getString(mContactCursor.getColumnIndex(this.photoUri));
                    String contactId = mContactCursor.getString(mContactCursor.getColumnIndex(this.contactId));
                    String address = mContactCursor.getString(mContactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    /*String normalizedAddress = PhoneNumberUtils.normalizeNumber(address);*/

                    ContactDetails contact = new ContactDetails();
                    contact.name = contactName;
                    contact.photoThumbUri = photoThumb;
                    contact.photoUri = photoUri;
                    contact.address = address;

                    String formattedAddress = phoneUtils.formatNumber(context, contact.address);
                    mContactMap.put(formattedAddress, contact);

                    //TODO Default country to format number

                    log.info(methodName, "Found contactName: " + contactName + " for thread_id: " + threadId + " In new map: " + doneSet.contains(threadId));
                }
                mContactCursor.close();
            } else {
                log.error(methodName, "Nothing in Contacts Cursor for " + address);
            }
        }
        else{
            log.error(methodName, "No contact read permissions");
        }
        //-- Create a map of all contacts Ends

        //Update Photo in Database Starts
        for(String key: addressDoneSet){
            String formattedKey = phoneUtils.formatNumber(context, key);
            ContactDetails contact = mContactMap.get(formattedKey);

            if(contact == null){ //In case address does not exists in Contacts
                continue;
            }

            ContentValues values = new ContentValues();
            values.put(this.contactName_local, contact.name);
            values.put(this.photoUri_local, contact.photoUri);
            values.put(this.photoUriThumbnail_local, contact.photoThumbUri);

            String whereClause = this.address_local + " LIKE ?";
            String encodedAddress = phoneUtils.encode(contact.address);
            

            String[] whereArgs = {encodedAddress};
            writableDB.update(Converesation.TABLE_NAME, values, whereClause, whereArgs);

        }
        //-- Update Photo in Database Ends

        // Update unread_count in Conversation Starts
        StringBuilder sbRawQuery = new StringBuilder();
        sbRawQuery.append(" UPDATE ");
        sbRawQuery.append(Converesation.TABLE_NAME);
        sbRawQuery.append(" SET "+ this.unreadCount_local+"= CASE "+ this.threadId_local);

        ArrayList<Integer> alValues = new ArrayList<>();
        Set<String> threadIds = unreadCountMap.keySet();

        for(String mThreadIds: threadIds){
            Integer iUnreadCount = unreadCountMap.get(mThreadIds);
            int unreadCount = iUnreadCount==null ? 0 : iUnreadCount;
            sbRawQuery.append(" WHEN '"+mThreadIds+"' THEN "+unreadCount);
            alValues.add(unreadCount);
        }
        sbRawQuery.append(" END WHERE ");
        sbRawQuery.append(Converesation.COLUMN_NAME_ADDRESS);
        sbRawQuery.append(" IN (");
        for(String mAddress: threadIds){
            sbRawQuery.append("'"+mAddress+"',");
        }

        sbRawQuery.deleteCharAt(sbRawQuery.length()-1); //Deleting , at end
        sbRawQuery.append(")");

        if(threadIds.size()>0) { //Updating empty database does not make sense and App crashes as well
            String[] strArgs = null;

            String rawQuery = sbRawQuery.toString();
            log.debug(methodName, "Formed Query: " + rawQuery);
            Cursor c = writableDB.rawQuery(rawQuery, strArgs);

            log.info(methodName, "Updated rows: " + c.getCount());
            c.close(); //Query won't update unless moveToFirst() or close() is called
        }
        //-- Update unread_count in Conversation Starts


        //Delete SMS From Conversation DB if not found in latest SMS DB
        Set<String> deletedThreads = oldMap.keySet();
        StringBuilder sbDeleteQuery = new StringBuilder();
        sbDeleteQuery.append(threadId_local+" IN (");
        String whereArgs[] = new String[deletedThreads.size()];
        int size = deletedThreads.size();
        for(String threadId: deletedThreads){
            sbDeleteQuery.append("?");
            if(--size > 0) {
                sbDeleteQuery.append(",");
            }
            whereArgs[size] = threadId;
        }
        sbDeleteQuery.append(")");

        String whereQuery = sbDeleteQuery.toString();
        log.debug(methodName, "Formed Query: "+whereQuery);
        writableDB.delete(DBConstants.TABLE_CONVERSATION, whereQuery, whereArgs);

        writableDB.close();
        mDBHelper.close();
        log.returning(methodName);
        return result;
    }


    class ContactDetails{
        String name, photoUri, photoThumbUri, address, id;
    }
}
