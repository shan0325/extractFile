package com.extract.util;

public class GitUtil {
    private CmdUtil cmdUtil;
    private String originBranch;

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
        return this.cmdUtil.exec(checkBranchCommand).trim();
    }

    public boolean isExistOriginMaster(String projectPath) {
        String checkOriginBranchCommand = "git -C " + projectPath + " branch -r";
        String originBranchs = this.cmdUtil.exec(checkOriginBranchCommand);

        if (originBranchs.contains("origin/master")) {
            this.originBranch = "master";
            return true;
        } else if (originBranchs.contains("origin/main")) {
            this.originBranch = "main";
            return true;
        }
        return false;
    }

    public String getFileListByDiffOriginMaster(String projectPath) {
        String gitDiffCommand = "git -C " + projectPath + " diff origin/" + this.originBranch + ".." + getCurrentBranch(projectPath) + " --name-only";
        return this.cmdUtil.exec(gitDiffCommand);
    }

}
