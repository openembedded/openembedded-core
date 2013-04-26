require binutils-cross_${PV}.bb

inherit crosssdk

PROVIDES = "virtual/${TARGET_PREFIX}binutils-crosssdk"

SRC_URI += "file://relocatable_sdk.patch"

do_configure_prepend () {
	sed -i 's#/usr/local/lib /lib /usr/lib#${SDKPATHNATIVE}/lib ${SDKPATHNATIVE}/usr/lib /usr/local/lib /lib /usr/lib#' ${S}/ld/configure.tgt
}
