SUMMARY = "Userspace framebuffer boot logo based on usplash."
DESCRIPTION = "PSplash is a userspace graphical boot splash screen for mainly embedded Linux devices supporting a 16bpp or 32bpp framebuffer. It has few dependencies (just libc), supports basic images and text and handles rotation. Its visual look is configurable by basic source changes. Also included is a 'client' command utility for sending information to psplash such as boot progress information."
HOMEPAGE = "http://git.yoctoproject.org/cgit/cgit.cgi/psplash"
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://psplash.h;beginline=1;endline=16;md5=840fb2356b10a85bed78dd09dc7745c6"

SRCREV = "e05374aae945bcfc6d962ed0d7b2774b77987e1d"
PV = "0.1+git${SRCPV}"
PR = "r2"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://psplash-init \
           ${SPLASH_IMAGES}"

SPLASH_IMAGES = "file://psplash-poky-img.h;outsuffix=default"

python __anonymous() {
    oldpkgs = d.getVar("PACKAGES", True).split()
    splashfiles = d.getVar('SPLASH_IMAGES', True).split()
    pkgs = []
    localpaths = []
    haspng = False
    for uri in splashfiles:
        fetcher = bb.fetch2.Fetch([uri], d)
        flocal = fetcher.localpath(uri)
        fbase = os.path.splitext(os.path.basename(flocal))[0]
        outsuffix = fetcher.ud[uri].parm.get("outsuffix")
        if not outsuffix:
            if fbase.startswith("psplash-"):
                outsuffix = fbase[8:]
            else:
                outsuffix = fbase
            if outsuffix.endswith('-img'):
                outsuffix = outsuffix[:-4]
        outname = "psplash-%s" % outsuffix
        if outname == '' or outname in oldpkgs:
            bb.fatal("The output name '%s' derived from the URI %s is not valid, please specify the outsuffix parameter" % (outname, uri))
        else:
            pkgs.append(outname)
        if flocal.endswith(".png"):
            haspng = True
        localpaths.append(flocal)

    # Set these so that we have less work to do in do_compile and do_install_append
    d.setVar("SPLASH_INSTALL", " ".join(pkgs))
    d.setVar("SPLASH_LOCALPATHS", " ".join(localpaths))

    if haspng:
        d.appendVar("DEPENDS", " gdk-pixbuf-native")

    d.prependVar("PACKAGES", "%s " % (" ".join(pkgs)))
    for p in pkgs:
        d.setVar("FILES_%s" % p, "${bindir}/%s" % p)
        d.setVar("ALTERNATIVE_PATH", "${bindir}/%s" % p)
        d.setVar("ALTERNATIVE_PRIORITY", "100")
        postinst = d.getVar("psplash_alternatives_postinst", True)
        d.setVar('pkg_postinst_%s' % p, postinst)
        postrm = d.getVar("psplash_alternatives_postrm", True)
        d.setVar('pkg_postrm_%s' % p, postrm)
        d.appendVar("RDEPENDS_%s" % p, " ${PN}")
        if p == "psplash-default":
            d.appendVar("RRECOMMENDS_${PN}", " %s" % p)
}

S = "${WORKDIR}/git"

inherit autotools pkgconfig update-rc.d

python do_compile () {
    import shutil, commands

    # Build a separate executable for each splash image
    destfile = "%s/psplash-poky-img.h" % d.getVar('S', True)
    localfiles = d.getVar('SPLASH_LOCALPATHS', True).split()
    outputfiles = d.getVar('SPLASH_INSTALL', True).split()
    for localfile, outputfile in zip(localfiles, outputfiles):
        if localfile.endswith(".png"):
            outp = commands.getstatusoutput('./make-image-header.sh %s POKY' % localfile)
            print(outp[1])
            fbase = os.path.splitext(os.path.basename(localfile))[0]
            shutil.copyfile("%s-img.h" % fbase, destfile)
        else:
            shutil.copyfile(localfile, destfile)
        # For some reason just updating the header is not enough, we have to touch the .c
        # file in order to get it to rebuild
        os.utime("psplash.c", None)
        bb.build.exec_func("oe_runmake", d)
        shutil.copyfile("psplash", outputfile)
}

do_install_append() {
	install -d ${D}/mnt/.psplash/
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/psplash-init ${D}${sysconfdir}/init.d/psplash.sh
	install -d ${D}${bindir}
	for i in ${SPLASH_INSTALL} ; do
		install -m 0755 $i ${D}${bindir}/$i
	done
	rm -f ${D}${bindir}/psplash
}

psplash_alternatives_postinst() {
update-alternatives --install ${bindir}/psplash psplash ${ALTERNATIVE_PATH} ${ALTERNATIVE_PRIORITY}
}

psplash_alternatives_postrm() {
update-alternatives --remove psplash ${ALTERNATIVE_PATH}
}

FILES_${PN} += "/mnt/.psplash"

INITSCRIPT_NAME = "psplash.sh"
INITSCRIPT_PARAMS = "start 0 S . stop 20 0 1 6 ."
