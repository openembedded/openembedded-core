require binutils-cross_${PV}.bb

inherit crosssdk

PN = "binutils-crosssdk-${SDK_SYS}"

PROVIDES = "virtual/${TARGET_PREFIX}binutils-crosssdk"

SRC_URI += "file://0001-binutils-crosssdk-Generate-relocatable-SDKs.patch"

do_configure_prepend () {
	sed -i 's#/usr/local/lib /lib ${libdir}#${SDKPATHNATIVE}/lib ${SDKPATHNATIVE}${libdir} /usr/local/lib /lib ${libdir}#' ${S}/ld/configure.tgt
}
