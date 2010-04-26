SRC_URI = "http://download.librdf.org/source/redland-1.0.8.tar.gz \
           file://crosscompile.patch;patch=1 \
	   file://sane_pkgconfig.patch;patch=1"
	   
PR = "r3"

EXTRA_OECONF = "--with-bdb-lib=${STAGING_LIBDIR} --with-bdb-include=${STAGING_INCDIR} --with-sqlite=no"

inherit autotools
