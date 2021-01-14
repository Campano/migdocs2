import java.util.*;
import java.io.File;
import java.util.regex.Pattern;

/**
 * Shared code TrnFsSyncTool
 */
public class CheckDocs2 implements java.io.Serializable {
	private static final long serialVersionUID = 1L;	
	private final Pattern PATTERN_CATEGORY = Pattern.compile("^CTG_[0-9]+_[a-z\\-]+$");
	private final Pattern PATTERN_LESSON = Pattern.compile("^LSN_[0-9]+_[a-z\\-]+$");
	private final File contentDir;

	public class TrnSyncException extends Exception{
		private String additional;
		public TrnSyncException(String msg){
			super(msg);
			additional = "";
		}
		
		public TrnSyncException(String msg, String additional){
			super(msg);
			this.additional = additional;
		}
		
		public TrnSyncException(String msg, File f){
			super(msg);
			this.additional = f.getPath();
		}
		
		public String getMessage(){
			return super.getMessage()+" : "+additional;
		}
	}

	public CheckDocs2(String docs2Path) throws TrnSyncException{
        contentDir = new File(docs2Path);
        verifyContentStructure();
	}
	
	public void verifyContentStructure() throws TrnSyncException{
		verifyFolderStructure(contentDir, true);
	}
	
	private void verifyFolderStructure(File dir, boolean isRoot) throws TrnSyncException{
		if(!dir.isDirectory())
			throw new TrnSyncException("TRN_SYNC_NOT_DIR", dir);
			
		if(isRoot)
			validateRootContent(dir);
		else if(isLesson(dir))
			validateLessonContent(dir);
		else if(isCategory(dir))
			validateCategoryContent(dir);
		else
			throw new TrnSyncException("TRN_SYNC_UNKNOWN_DIR_TYPE", dir);
		
		if(isRoot || isCategory(dir))	
			for(File child : dir.listFiles())
				if(child.isDirectory())
					verifyFolderStructure(child, false);
	}
	
	private void validateRootContent(File dir) throws TrnSyncException{
		for(File child : dir.listFiles())
			if(!isCategory(child))
				throw new TrnSyncException("TRN_SYNC_ROOT_NON_CONFORMITY", child);
	}
	
	private void validateCategoryContent(File dir) throws TrnSyncException{
		boolean hasJson = false;
		for(File child : dir.listFiles()){
			if("category.json".equals(child.getName()))
				hasJson = true;
			else if(!isCategory(child) && !isLesson(child))
				throw new TrnSyncException("TRN_SYNC_CATEGORY_NON_CONFORMITY", child);
		}
		if(!hasJson)
			throw new TrnSyncException("TRN_SYNC_CATEGORY_JSON_MISSING", dir);
	}
	
	private void validateLessonContent(File dir) throws TrnSyncException{
		String lessonCode = dir.getName().split("_")[2];
		boolean hasJson = false;
		boolean hasMd = false;
		boolean hasWebm = false;
		for(File child : dir.listFiles()){
			if("lesson.json".equals(child.getName()))
				hasJson = true;
			else if(child.isDirectory())
				throw new TrnSyncException("TRN_SYNC_LESSON_CONTAINING_FOLDER", child);
			else if(child.getName().equals(lessonCode+".md"))
				hasMd = true;
			else if(getExtension(child.getName()).equals("md")){
				hasMd = true;
				if(!child.getName().equals(lessonCode+".md"))
					throw new TrnSyncException("TRN_SYNC_LESSON_MARDOWN_NAME_INCONSISTENT", child);
			}
			else if(getExtension(child.getName()).equals("webm")){
				hasWebm = true;
				if(!child.getName().equals(lessonCode+".webm"))
					throw new TrnSyncException("TRN_SYNC_LESSON_VIDEO_NAME_INCONSISTENT", child);
			}
			else if(!getExtension(child.getName()).equals("png")){
				throw new TrnSyncException("TRN_SYNC_LESSON_EXTENSION_NOT_ALLOWED", child);
			}
		}
		if(!hasJson)
			throw new TrnSyncException("TRN_SYNC_LESSON_JSON_MISSING", dir);
    }
    
    public static String getExtension(String path)
	{
		String n = getName(path);
		return n.substring(n.lastIndexOf('.') + 1).toLowerCase();
	}
    
    public static String getName(String path)
	{
		return new File(path).getName().trim();
	}
    
	private boolean isCategory(File dir){
		return dir.isDirectory() && PATTERN_CATEGORY.matcher(dir.getName()).matches();
	}
	
	private boolean isLesson(File dir){
		return dir.isDirectory() && PATTERN_LESSON.matcher(dir.getName()).matches();
	}
	
}
