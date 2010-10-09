require dpkg.inc

SRC_URI += "file://noman.patch;patch=1 \
            file://check_snprintf.patch \
            file://check_version.patch"

EXTRA_OECONF = "--without-static-progs \
		--without-dselect \
		--with-start-stop-daemon \
		--with-zlib \
		--with-bz2lib \
		--without-selinux \
		--without-sgml-doc"

BBCLASSEXTEND = "native"
