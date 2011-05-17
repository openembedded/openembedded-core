DESCRIPTION = "A nasty hack for for dtl1-cs driver to workaround suspend/resume."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
SECTION = "console" 
PRIORITY = "optional" 
PR = "r3"
 
SRC_URI = "file://02dtl1_cs.sh \
           file://COPYING.patch"

inherit allarch

do_install() { 
        install -d ${D}${sysconfdir}/apm/event.d/ 
        install -m 0755 ${WORKDIR}/02dtl1_cs.sh ${D}${sysconfdir}/apm/event.d/ 
} 

#Package 02dtl1_cs.sh, which is a nasty hack to get dtl1c_cs cards working with suspend/resume
FILES_${PN} += "${sysconfdir}/apm/"
