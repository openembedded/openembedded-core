DESCRIPTION = "A nasty hack for for dtl1-cs driver to workaround suspend/resume."
SECTION = "console" 
PRIORITY = "optional" 
LICENSE = "GPL" 
PR = "r1"
 
SRC_URI = "file://02dtl1_cs.sh"

do_install() { 
        install -d ${D}${sysconfdir}/apm/event.d/ 
        install -m 0755 ${WORKDIR}/02dtl1_cs.sh ${D}${sysconfdir}/apm/event.d/ 
} 

#Package 02dtl1_cs.sh, which is a nasty hack to get dtl1c_cs cards working with suspend/resume
FILES_${PN} += "${sysconfdir}/apm/"

PACKAGE_ARCH = "all"
