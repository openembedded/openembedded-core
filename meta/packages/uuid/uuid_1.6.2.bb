SRC_URI = "ftp://ftp.ossp.org/pkg/lib/uuid/uuid-1.6.2.tar.gz \
           file://fixes.patch;patch=1"
PR = "r0"

inherit autotools

do_configure_prepend () {
	rm libtool.m4
}

export LIBTOOL = "${S}/${TARGET_PREFIX}libtool"

do_stage () {
	autotools_stage_all
}