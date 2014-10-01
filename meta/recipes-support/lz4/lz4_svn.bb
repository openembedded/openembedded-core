SUMMARY = "Extremely Fast Compression algorithm"
DESCRIPTION = "LZ4 is a very fast lossless compression algorithm, providing compression speed at 400 MB/s per core, scalable with multi-cores CPU. It also features an extremely fast decoder, with speed in multiple GB/s per core, typically reaching RAM speed limits on multi-core systems."

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0b0d063f37a4477b54af2459477dcafd \
                    file://Makefile;md5=7551cc5d31ca66e003ab06e83486524a"

# Upstream names releases after SVN revs
SRCREV = "123"
PV = "r${SRCREV}"

SRC_URI = "svn://lz4.googlecode.com/svn/;module=trunk;protocol=http"

S = "${WORKDIR}/trunk"

EXTRA_OEMAKE = "PREFIX=${prefix} CC='${CC}' DESTDIR=${D} LIBDIR=${libdir} INCLUDEDIR=${includedir}"

do_install() {
	oe_runmake install
}

BBCLASSEXTEND += "native nativesdk"
