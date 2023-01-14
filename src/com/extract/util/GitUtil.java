package com.extract.util;

import javax.swing.*;

public class GitUtil {
    private CmdUtil cmdUtil;

    public GitUtil(CmdUtil cmdUtil) {
        this.cmdUtil = cmdUtil;
    }

    public boolean isInstalledGit() {
        String gitVersion = this.cmdUtil.exec("git --version");
        if (StringUtils.isEmpty(gitVersion) || !gitVersion.contains("git version")) {
            return false;
        }
        return true;
    }

    public String getCurrentBranch(String projectPath) {
        String checkBranchCommand = "git -C " + projectPath + " branch --show-current";
        return this.cmdUtil.exec(checkBranchCommand);
    }

    public boolean isExistOriginMaster(String projectPath) {
        String checkOriginBranchCommand = "git -C " + projectPath + " branch -r";
        String originBranchs = this.cmdUtil.exec(checkOriginBranchCommand);

        if (!originBranchs.contains("origin/master")) {
            return false;
        }
        return true;
    }

    public String getFileListByDiffOriginMaster(String projectPath) {
        String gitDiffCommand = "git -C " + projectPath + " diff origin/master.." + getCurrentBranch(projectPath) + " --name-only";
        return this.cmdUtil.exec(gitDiffCommand);
    }

}
