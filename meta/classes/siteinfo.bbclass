# This class exists to provide information about the targets that
# may be needed by other classes and/or recipes. If you add a new
# target this will probably need to be updated.

#
# Returns information about 'what' for the named target 'target'
# where 'target' == "<arch>-<os>"
#
# 'what' can be one of
# * target: Returns the target name ("<arch>-<os>")
# * endianess: Return "be" for big endian targets, "le" for little endian
# * bits: Returns the bit size of the target, either "32" or "64"
# * libc: Returns the name of the c library used by the target
#
# It is an error for the target not to exist.
# If 'what' doesn't exist then an empty value is returned
#
def siteinfo_data(d):
    archinfo = {
        "allarch": "endian-little bit-32", # bogus, but better than special-casing the checks below for allarch
        "arm": "endian-little bit-32 arm-common",
        "armeb": "endian-big bit-32 arm-common",
        "avr32": "endian-big bit-32 avr32-common",
        "bfin": "endian-little bit-32 bfin-common",
        "i386": "endian-little bit-32 ix86-common",
        "i486": "endian-little bit-32 ix86-common",
        "i586": "endian-little bit-32 ix86-common",
        "i686": "endian-little bit-32 ix86-common",
        "ia64": "endian-little bit-64",
        "microblaze": "endian-big bit-32 microblaze-common",
        "microblazeel": "endian-little bit-32 microblaze-common",
        "mips": "endian-big bit-32 mips-common",
        "mips64": "endian-big bit-64 mips64-common",
        "mips64el": "endian-little bit-64 mips64-common",
        "mipsel": "endian-little bit-32 mips-common",
        "powerpc": "endian-big bit-32 powerpc-common powerpc32-linux",
        "nios2": "endian-little bit-32 nios2-common",
        "powerpc64": "endian-big bit-64 powerpc-common powerpc64-linux",
        "ppc": "endian-big bit-32 powerpc-common powerpc32-linux",
        "ppc64": "endian-big bit-64 powerpc-common powerpc64-linux",
        "sh3": "endian-little bit-32 sh-common",
        "sh4": "endian-little bit-32 sh-common",
        "sparc": "endian-big bit-32",
        "viac3": "endian-little bit-32 ix86-common",
        "x86_64": "endian-little bit-64",
    }
    osinfo = {
        "darwin": "common-darwin",
        "darwin9": "common-darwin",
        "linux": "common-linux common-glibc",
        "linux-gnueabi": "common-linux common-glibc",
        "linux-gnuspe": "common-linux common-glibc",
        "linux-uclibc": "common-linux common-uclibc",
        "linux-uclibceabi": "common-linux common-uclibc",
        "linux-uclibcspe": "common-linux common-uclibc",
        "uclinux-uclibc": "common-uclibc",
        "cygwin": "common-cygwin",
        "mingw32": "common-mingw",
    }
    targetinfo = {
        "arm-linux-gnueabi": "arm-linux",
        "arm-linux-uclibceabi": "arm-linux-uclibc",
        "armeb-linux-gnueabi": "armeb-linux",
        "armeb-linux-uclibceabi": "armeb-linux-uclibc",
        "powerpc-linux-gnuspe": "powerpc-linux",
        "powerpc-linux-uclibcspe": "powerpc-linux-uclibc",
    }

    hostarch = d.getVar("HOST_ARCH", True)
    hostos = d.getVar("HOST_OS", True)
    target = "%s-%s" % (hostarch, hostos)

    sitedata = []
    if hostarch in archinfo:
        sitedata.extend(archinfo[hostarch].split())
    if hostos in osinfo:
        sitedata.extend(osinfo[hostos].split())
    if target in targetinfo:
        sitedata.extend(targetinfo[target].split())
    sitedata.append(target)
    sitedata.append("common")

    bb.debug(1, "SITE files %s" % sitedata);
    return sitedata

python () {
    sitedata = set(siteinfo_data(d))
    if "endian-little" in sitedata:
        d.setVar("SITEINFO_ENDIANESS", "le")
    elif "endian-big" in sitedata:
        d.setVar("SITEINFO_ENDIANESS", "be")
    else:
        bb.error("Unable to determine endianness for architecture '%s'" %
                 d.getVar("HOST_ARCH", True))
        bb.fatal("Please add your architecture to siteinfo.bbclass")

    if "bit-32" in sitedata:
        d.setVar("SITEINFO_BITS", "32")
    elif "bit-64" in sitedata:
        d.setVar("SITEINFO_BITS", "64")
    else:
        bb.error("Unable to determine bit size for architecture '%s'" %
                 d.getVar("HOST_ARCH", True))
        bb.fatal("Please add your architecture to siteinfo.bbclass")
}

def siteinfo_get_files(d):
    sitedata = siteinfo_data(d)
    sitefiles = ""
    for path in d.getVar("BBPATH", True).split(":"):
        for element in sitedata:
            filename = os.path.join(path, "site", element)
            if os.path.exists(filename):
                sitefiles += filename + " "

    # Now check for siteconfig cache files
    path_siteconfig = bb.data.getVar('SITECONFIG_SYSROOTCACHE', d, 1)
    if os.path.isdir(path_siteconfig):
        for i in os.listdir(path_siteconfig):
            filename = os.path.join(path_siteconfig, i)
            sitefiles += filename + " "

    return sitefiles

#
# Make some information available via variables
#
SITECONFIG_SYSROOTCACHE = "${STAGING_DATADIR}/${TARGET_SYS}_config_site.d"
