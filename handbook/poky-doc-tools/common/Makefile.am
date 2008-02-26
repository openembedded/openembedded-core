SUPPORT_FILES = VeraMoBd.ttf              VeraMoBd.xml            \
         	VeraMono.ttf              VeraMono.xml            \
         	Vera.ttf                  Vera.xml                \
                draft.png                 titlepage.templates.xml \
	        poky-db-pdf.xsl           poky.svg                \
    	        ohand-color.svg           poky-handbook.png

commondir = $(pkgdatadir)/common
common_DATA = $(SUPPORT_FILES) fop-config.xml

EXTRA_DIST = $(SUPPORT_FILES) fop-config.xml.in

edit = sed -e 's,@datadir\@,$(pkgdatadir),g' 

fop-config.xml: fop-config.xml.in
	rm -f fop-config.xml
	$(edit) fop-config.xml.in > fop-config.xml


clean-local:
	rm -fr fop-config.xml