package framework_mechanism;

public class DependencyContainer {

    private String qualifierClassName;
    private Class qualifierClass;

    public DependencyContainer(String qualifierClassName, Class qualifierClass) {
        this.qualifierClassName = qualifierClassName;
        this.qualifierClass = qualifierClass;
    }

    public String getQualifierClassName() {
        return qualifierClassName;
    }

    public Class getQualifierClass() {
        return qualifierClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DependencyContainer that = (DependencyContainer) o;

        if (!qualifierClassName.equals(that.qualifierClassName)) return false;
        return qualifierClass.equals(that.qualifierClass);
    }

    @Override
    public int hashCode() {
        int result = qualifierClassName.hashCode();
        result = 31 * result + qualifierClass.hashCode();
        return result;
    }
}
