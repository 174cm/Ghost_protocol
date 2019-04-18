package server;
/**
 * 
 * @author 고명주
 * @brief 게시물 하나하나 저장하는 클래스
 */
public class BoardDTO {

	private String name;
	private String pwd;
	private String title;
	private String contents;
	private String categorize;
	
	/**
	 * @brief 닉네임
	 * @return Text필드에서 닉네임 받아와서 Retrun
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @brief 비밀번호
	 * @return Password필드에서 비밀번호 받아와서 return
	 */
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * @brief 제목
	 * @return text필드에서 제목 받아와서 return
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @brief 내용
	 * @return textarea에서 내용 받아와서 return 
	 */
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	/**
	 * @brief 분류
	 * @return 콤보 박스에서 분류 받아와서 return
	 */
	public String getCategorize() {
		return categorize;
	}
	public void setCategorize(String categorize) {
		this.categorize = categorize;
	}
	
	
	@Override
	public String toString() {
		return "BoardDTO [name=" + name + ", pwd=" + pwd + ", title=" + title + ", contents=" + contents
				+ ", categorize=" + categorize + "]";
	}
	
	

}
