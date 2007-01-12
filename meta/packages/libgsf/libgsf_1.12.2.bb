LICENSE = "GPL"
SECTION = "libs"
PR = "r0"

DEPENDS= "libxml2 glib-2.0 zlib gtk-doc libbonobo gnome-vfs"

PACKAGES =+ "${PN}-gnome ${PN}-gnome-dev "

FILES_${PN}-gnome = "${libdir}/libgsf-gnome-1.so.*"
FILES_${PN}-gnome-dev = "${libdir}/libgsf-gnome-1.* ${includedir}/libgsf-1/gsf-gnome"

inherit autotools pkgconfig gnome

libgsf_includes = "gsf-doc-meta-data.h gsf-infile.h gsf-input-textline.h \
		   gsf-outfile-zip.h gsf-output-stdio.h gsf-impl-utils.h \
		   gsf-input-bzip.h gsf-input.h gsf-outfile.h gsf-output.h \
		   gsf-infile-impl.h gsf-input-gzip.h gsf-libxml.h \
		   gsf-output-bzip.h gsf-structured-blob.h gsf-infile-msole.h \
		   gsf-input-impl.h gsf-msole-utils.h gsf-output-gzip.h \
		   gsf-timestamp.h gsf-infile-msvba.h gsf-input-iochannel.h \
		   gsf-outfile-impl.h gsf-output-impl.h gsf-utils.h \
		   gsf-infile-stdio.h gsf-input-memory.h gsf-outfile-msole.h \
		   gsf-output-iochannel.h gsf.h gsf-infile-zip.h gsf-input-stdio.h \
		   gsf-outfile-stdio.h gsf-output-memory.h gsf-output-csv.h \
		   gsf-output-iconv.h gsf-meta-names.h "

libgsf_gnome_includes = "gsf-input-gnomevfs.h \
			 gsf-output-gnomevfs.h \
			 gsf-input-bonobo.h \
			 gsf-output-bonobo.h \
			 gsf-shared-bonobo-stream.h"

do_stage() {
	oe_libinstall -so -C gsf libgsf-1 ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/libgsf-1/gsf/
	for i in ${libgsf_includes}; do
		install -m 0644 gsf/$i ${STAGING_INCDIR}/libgsf-1/gsf/$i
	done

	oe_libinstall -so -C gsf-gnome libgsf-gnome-1 ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/libgsf-1/gsf-gnome/
	for i in ${libgsf_gnome_includes}; do
		install -m 0644 gsf-gnome/$i ${STAGING_INCDIR}/libgsf-1/gsf-gnome/$i
	done
}
