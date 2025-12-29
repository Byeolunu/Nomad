package nutar.back.dao.enums;
public enum SkillCategory
{
    PROGRAMMING("Programming & Development"),
    DESIGN("Design & Creative"),
    WRITING("Writing & Translation"),
    MARKETING("Marketing & Sales"),
    ADMIN_SUPPORT("Admin & Support"),
    FINANCE("Finance & Accounting"),
    ENGINEERING("Engineering & Architecture"),
    BUSINESS("Business & Consulting"),
    LEGAL("Legal"),
    CUSTOMER_SERVICE("Customer Service"),
    DATA_SCIENCE("Data Science & Analytics"),
    MOBILE("Mobile Development"),
    DEVOPS("DevOps & Infrastructure"),
    GAME_DEV("Game Development"),
    BLOCKCHAIN("Blockchain"),
    AI_ML("AI & Machine Learning");
    private final String displayName;
    SkillCategory(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
