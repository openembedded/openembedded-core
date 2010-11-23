DESCRIPTION = "The Bitstream Vera fonts - TTF Edition"
SECTION = "x11/fonts"
PRIORITY = "optional"
LICENSE = "Bitstream Vera"
LIC_FILES_CHKSUM = "file://COPYRIGHT.TXT;md5=27d7484b1e18d0ee4ce538644a3f04be"
PACKAGE_ARCH = "all"
PR = "r3"
RDEPENDS = "fontconfig-utils"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/ttf-bitstream-vera/1.10/ttf-bitstream-vera-${PV}.tar.bz2" 

do_install () { 
        install -d ${D}${prefix}/share/fonts/ttf/ 
        for i in *.ttf; do 
                install -m 644 $i ${D}${prefix}/share/fonts/ttf/${i} 
        done 

	# fontconfig ships this too.  not sure what to do about it.
        #install -d ${D}${sysconfdir}/fonts 
        #install -m 644 local.conf ${D}${sysconfdir}/fonts/local.conf 


        install -d ${D}${prefix}/share/doc/${PN}/ 
        for i in *.TXT; do 
                install -m 644 $i ${D}${prefix}/share/doc/${PN}/$i 
        done 
} 

pkg_postinst () {
#!/bin/sh
fc-cache
}


FILES_${PN} = "/etc ${datadir}/fonts"
