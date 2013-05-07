package mine.emf1002.rbac.model.view;

import mine.emf1002.annotation.FieldInfo;
import mine.emf1002.annotation.NodeType;
import mine.emf1002.constant.TreeNodeType;
import mine.emf1002.model.TreeBaseEntity;
import mine.emf1002.rbac.model.Department;


public class VDeptUser extends TreeBaseEntity {
	@NodeType(type=TreeNodeType.ID)
	@FieldInfo(name="主键",type="ID")
	private String id;
	@NodeType(type=TreeNodeType.TEXT)
	@FieldInfo(name="名称")
	private String text;
	@NodeType(type=TreeNodeType.CODE)
	@FieldInfo(name="编码")
	private String code;
	@NodeType(type=TreeNodeType.DISABLED)
	@FieldInfo(name="是否不可选")
	private String disabled;
	@NodeType(type=TreeNodeType.ICON)
	@FieldInfo(name="图标")
	private String icon;
	@NodeType(type=TreeNodeType.PARENT)
	@FieldInfo(name="图标")
	private Department parent;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public Department getParent() {
		return parent;
	}
	public void setParent(Department parent) {
		this.parent = parent;
	}
	
}
