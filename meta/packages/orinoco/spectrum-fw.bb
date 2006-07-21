DESCRIPTION = "Firmware for Spectrum Wireless LAN cards"
LICENSE = "unknown"
PR = "r0"
PACKAGE_ARCH = "all"

SRC_URI = "file://get_symbol_fw \
           file://parse_symbol_fw"

S = "${WORKDIR}"

do_compile() {
	./get_symbol_fw
}

FILES_${PN} += "${base_libdir}/firmware/symbol*"

do_install() {
	install -d ${D}${base_libdir}/firmware/
	install -m 0755 ${WORKDIR}/symbol_sp24t_prim_fw ${D}${base_libdir}/firmware/symbol_sp24t_prim_fw
	install -m 0755 ${WORKDIR}/symbol_sp24t_sec_fw ${D}${base_libdir}/firmware/symbol_sp24t_sec_fw
}
