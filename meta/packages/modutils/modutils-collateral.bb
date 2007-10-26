SECTION = "base"
DESCRIPTION = "modutils configuration files"
PR = "r3"
LICENSE = "MIT"

SRC_URI = "file://modules \
	   file://modules.conf"

do_compile () {
}

do_install () {
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/modules ${D}${sysconfdir}/modules
        if [ ${MAJOR_KERNEL_VERSION}=2.6 ]; then
  	 install -d ${D}${sysconfdir}/modprobe.d         
        else
 	 install -m 0644 ${WORKDIR}/modules.conf ${D}${sysconfdir}/modules.conf
        fi

}
