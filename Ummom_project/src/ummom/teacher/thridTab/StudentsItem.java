package ummom.teacher.thridTab;

public class StudentsItem {
	
	private String name;
	private String birth;
	private String phone;
	private int img;
	private String id;
	
	public StudentsItem(String id, int img, String name,String birth, String phone){
		this.name = name;
		this.birth = birth;
		this.phone = phone;
		this.img = img;
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

}
