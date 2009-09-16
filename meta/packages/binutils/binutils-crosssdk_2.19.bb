require binutils-cross_${PV}.bb

inherit crosssdk

PROVIDES = "virtual/${TARGET_PREFIX}binutils-crosssdk"

PR = "r1"

do_configure_prepend () {
	sed -i 's#/usr/local/lib /lib /usr/lib#${SDKPATH}/lib /usr/local/lib /lib /usr/lib#' ${S}/ld/configure.tgt
}

