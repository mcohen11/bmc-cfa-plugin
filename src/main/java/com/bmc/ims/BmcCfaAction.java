package com.bmc.ims;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import hudson.FilePath;
import hudson.model.Run;
import io.jenkins.plugins.util.AbstractXmlStream;
import io.jenkins.plugins.util.BuildAction;
import io.jenkins.plugins.util.JobAction;
import org.kohsuke.stapler.StaplerProxy;

import java.io.File;
//import hudson.model.Run;
//@Extension
//@ExportedBean
//@SuppressWarnings("PMD.DataClass")
//public class BmcCfaAction implements BuildBadgeAction, RunAction2 {
//public class BmcCfaAction implements RunAction2 {
public class BmcCfaAction extends BuildAction implements StaplerProxy {
    //private transient Run run;
    //private JSONArray ja;
    String ws;
    int buildNum;
    Run owner;
    String reportType,jobid, csvFileName;
    ResponseObject resp;
/*
    protected BmcCfaAction(Run owner, Object result)  {

          super(owner, result, false);

        this.run=owner;
    }
*/
    public BmcCfaAction(Run owner, int number, String workspace, ResponseObject resp, String jobid, String csvFileName) {
        super(owner, resp, false);
        this.ws=workspace;
        this.buildNum=number;
        this.owner=owner;
        this.resp=resp;
        this.jobid=jobid;
        this.csvFileName=csvFileName;
    }


    @Override
    public String getIconFileName() {
        return "clipboard.png";
    }

    @Override
    public String getDisplayName() {
        return "Commit Distribution - "+this.jobid+" - "+ this.csvFileName;
    }

    @Override
    public String getUrlName() {
        return "stat"+this.jobid+this.csvFileName;
    }


     /**
      * Returns the commit distribution view.
      *
      * @return the commit distribution view
      */

     @Override
     @SuppressWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
     public Object getTarget() {

         return new ReportViewModel(this.owner,CsvFile.readCsvFile(getRelatedJob().getPath()),this.reportType);
     }


     @SuppressWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
     public File getRelatedJob() {


             //File dir = new File(((FreeStyleBuild) this.run).getWorkspace()+"\\"+this.run.getNumber());
             File dir = new File(this.ws + File.separator + this.buildNum + File.separator+ this.jobid);
            try {
                File[] matches = dir.listFiles((dir1, name) -> name.contains(this.csvFileName));

                    if (matches[0].getPath().contains("IMS") || matches[0].getPath().contains("DLI") )
                        this.reportType = "IMS";
                    else
                        this.reportType = "DB2";
                    return matches[0];

            }
            catch(NullPointerException ex){return dir;}


     }


    @Override
    @CheckForNull
    @SuppressWarnings("NP_NONNULL_RETURN_VIOLATION")
    protected AbstractXmlStream createXmlStream() {
        return null;
    }


    @Override
    @CheckForNull
    @SuppressWarnings("NP_NONNULL_RETURN_VIOLATION")
    protected JobAction<? extends BuildAction> createProjectAction() {
        return null;
    }


    @Override
    protected String getBuildResultBaseName() {
        return "cfa"+this.jobid+this.csvFileName;
    }
 }
