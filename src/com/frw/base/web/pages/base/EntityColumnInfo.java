package com.frw.base.web.pages.base;

import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.wicket.model.IModel;

public class EntityColumnInfo implements Serializable {
	
    public boolean canBeExported = true;
    private Integer sizeColumn;
    protected short aligment = HSSFCellStyle.ALIGN_LEFT;
    protected short background = HSSFColor.WHITE.index;
    protected boolean bold = false;
    protected boolean canBeHidden = false;
    protected boolean canBeSorted;

    protected boolean datahora = false;
    protected short foreground = HSSFColor.BLACK.index;
    protected boolean italic = false;
    /* header properties */
    protected IModel<String> name;
    protected boolean percent = false;
    protected String property;
    protected boolean visible;
    
    public EntityColumnInfo(String property, IModel<String> name) {
    	
    	this(property,name,false,true,true);
    	
    }
    
    public EntityColumnInfo(String property, IModel<String> name,boolean canBeSorted) {
    	this.name = name;
    	this.property = property;
    	this.visible = true;
    	this.canBeHidden=canBeHidden;
    	this.canBeSorted=canBeSorted;
    	
    }
    
    public EntityColumnInfo(String property, IModel<String> name,boolean canBeHidden,boolean canBeSorted) {
    	this(property, name, canBeSorted);
    	this.canBeExported = canBeExported;
    }
    
    public EntityColumnInfo(String property, IModel<String> name,boolean canBeHidden,boolean canBeSorted, boolean canBeExported) {
    	this(property, name, canBeHidden, canBeSorted);
    	this.canBeExported = canBeExported;
    }
    
    
    public EntityColumnInfo(String property, IModel<String> name,boolean canBeHidden,boolean canBeSorted, Integer sizeColumn) {
    	this(property, name, canBeHidden, canBeSorted);
    	this.sizeColumn = sizeColumn;
    }
    
    public EntityColumnInfo(String property, IModel<String> name,boolean canBeHidden,boolean canBeSorted, Integer sizeColumn, boolean canBeExported) {
    	this(property, name, canBeHidden, canBeSorted, sizeColumn);
    }
    
    public EntityColumnInfo(String property, IModel<String> name, short foreground, short background, short aligment, boolean bold, boolean italic) {
    	
    	this(property,name,false,true,true);
    	
    	this.foreground = foreground;
    	this.background = background;  
    	this.aligment = aligment;
    	this.bold = bold; 
    	this.italic = italic;
    	
    }
    
    public EntityColumnInfo(String property, IModel<String> name, short foreground, short background, short aligment, boolean bold, boolean italic, boolean datahora) {
    	this(property,name,foreground,background,aligment,bold, italic);
    	this.datahora = datahora;
    }
    
    public EntityColumnInfo(String property, IModel<String> name, short foreground, short background, short aligment, boolean bold, boolean italic, boolean datahora, boolean percent) {
    	this(property,name,foreground,background,aligment,bold, italic);
    	this.datahora = datahora;
    	this.percent = percent;
    }
    
    public short getAligment() {
		return aligment;
	}

    public short getBackground() {
		return background;
	}

    public boolean getBold() {
		return bold;
	}

    public short getForeground() {
		return foreground;
	}

    public boolean getItalic() {
		return italic;
	}

    public IModel<String> getName() {
        return name;
    }

    public String getProperty() {
        return property;
    }

    public Integer getSizeColumn() {
        return sizeColumn;
    }

    public boolean isCanBeExported() {
        return canBeExported;
    }

    public boolean isCanBeHidden() {
        return canBeHidden;
    }

    public boolean isCanBeSorted() {
        return canBeSorted;
    }

    public boolean isDatahora() {
		return datahora;
	}

    public boolean isVisible() {
        return visible;
    }

    public void setAligment(short aligment) {
		this.aligment = aligment;
	}



	public void setBackground(short background) {
		this.background = background;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public void setCanBeExported(boolean canBeExported) {
        this.canBeExported = canBeExported;
    }

	public void setCanBeHidden(boolean canBeHidden) {
        this.canBeHidden = canBeHidden;
    }

	public void setCanBeSorted(boolean canBeSorted) {
        this.canBeSorted = canBeSorted;
    }

	public void setDatahora(boolean datahora) {
		this.datahora = datahora;
	}

	public void setForeground(short foreground) {
		this.foreground = foreground;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public void setName(IModel<String> name) {
        this.name = name;
    }

	public void setProperty(String property) {
        this.property = property;
    }

	public void setSizeColumn(Integer sizeColumn) {
        this.sizeColumn = sizeColumn;
    }

	public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
