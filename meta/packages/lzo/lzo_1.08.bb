DESCRIPTION = "Lossless data compression library"
HOMEPAGE = "http://www.oberhumer.com/opensource/lzo/"
LICENSE = "GPLv2"
SECTION = "libs"
PRIORITY = "optional"
PR = "r14"

SRC_URI = "http://www.oberhumer.com/opensource/lzo/download/lzo-${PV}.tar.gz"

inherit autotools 

EXTRA_OECONF = "--enable-shared"

#do_configure () {
#	# override this function to avoid the autoconf/automake/aclocal/autoheader
#	# calls for now
#	gnu-configize
#	oe_runconf
#}

do_stage() {
	install -m 0644 include/lzo1.h ${STAGING_INCDIR}/lzo1.h
	install -m 0644 include/lzo16bit.h ${STAGING_INCDIR}/lzo16bit.h
	install -m 0644 include/lzo1a.h ${STAGING_INCDIR}/lzo1a.h
	install -m 0644 include/lzo1b.h ${STAGING_INCDIR}/lzo1b.h
	install -m 0644 include/lzo1c.h ${STAGING_INCDIR}/lzo1c.h
	install -m 0644 include/lzo1f.h ${STAGING_INCDIR}/lzo1f.h
	install -m 0644 include/lzo1x.h ${STAGING_INCDIR}/lzo1x.h
	install -m 0644 include/lzo1y.h ${STAGING_INCDIR}/lzo1y.h
	install -m 0644 include/lzo1z.h ${STAGING_INCDIR}/lzo1z.h
	install -m 0644 include/lzo2a.h ${STAGING_INCDIR}/lzo2a.h
	install -m 0644 include/lzoconf.h ${STAGING_INCDIR}/lzoconf.h
	install -m 0644 include/lzoutil.h ${STAGING_INCDIR}/lzoutil.h

        oe_libinstall -a -so -C src liblzo ${STAGING_LIBDIR}
}
