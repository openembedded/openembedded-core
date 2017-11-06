MAJOR_VERSION = "2.31"
require util-linux.inc

# To support older hosts, we need to patch and/or revert
# some upstream changes.  Only do this for native packages.
OLDHOST = ""
OLDHOST_class-native = "file://util-linux-native-qsort.patch"

SRC_URI += "file://configure-sbindir.patch \
            file://runuser.pamd \
            file://runuser-l.pamd \
            ${OLDHOST} \
            file://ptest.patch \
            file://run-ptest \
            file://display_testname_for_subtest.patch \
            file://avoid_parallel_tests.patch \
"
SRC_URI_append_class-native = " file://no_getrandom.patch"
SRC_URI[md5sum] = "5b6821c403c3cc6e7775f74df1882a20"
SRC_URI[sha256sum] = "f9be7cdcf4fc5c5064a226599acdda6bdf3d86c640152ba01ea642d91108dc8a"

CACHED_CONFIGUREVARS += "scanf_cv_alloc_modifier=ms"

EXTRA_OECONF_class-native = "${SHARED_EXTRA_OECONF} \
                             --disable-fallocate \
			     --disable-use-tty-group \
"
EXTRA_OECONF_class-nativesdk = "${SHARED_EXTRA_OECONF} \
                                --disable-fallocate \
				--disable-use-tty-group \
"
