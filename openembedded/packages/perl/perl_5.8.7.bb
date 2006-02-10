MAINTAINER="David Karlstrom <daka@thg.se>"

include perl.inc

SRC_URI += "file://config.sh-armeb-linux \
	    file://config.sh-arm-linux \
	    file://config.sh-i386-linux"

PR = "r14"

do_configure() {
	ln -sf ${HOSTPERL} ${STAGING_BINDIR}/hostperl
	cp ${HOSTPERL} hostperl
	cd Cross
	rm Makefile.SH.patch
	cp ${WORKDIR}/Makefile.SH.patch .
	cp ${WORKDIR}/config.sh-mipsel-linux .
	cp ${WORKDIR}/config.sh-i686-linux .
	cp ${WORKDIR}/config.sh-i386-linux .
	cp ${WORKDIR}/config.sh-armeb-linux .
	# nslu2 LE uclibc builds do not work with the default config.sh
	if test "${MACHINE}" = nslu2
	then
		rm -f ./config.sh-arm-linux
		cp ${WORKDIR}/config.sh-arm-linux .
	fi
	for i in config.sh-*-linux; do
		a="`echo $i|sed -e 's,^config.sh-,,; s,-linux$,,'`"
		newfile="`echo $i|sed -e 's,-linux$,-linux-uclibc,g'`"
		cat $i | sed -e "s,${a}-linux,${a}-linux-uclibc,g; \
		s,d_sockatmark='define',d_sockatmark='undef',g;" > $newfile
	done
	sed -i -e 's,./install_me_here,${D},g' config.sh-${TARGET_ARCH}-${TARGET_OS}
	rm -f config
	echo "ARCH = ${TARGET_ARCH}" > config
	echo "OS = ${TARGET_OS}" >> config
	oe_runmake patch
}

do_install_append() {
	ln -s libperl.so.${PV} ${D}/${libdir}/libperl.so.5
	sed -i -e "s,${D},,g" ${D}/${libdir}/perl5/${PV}/${TARGET_ARCH}-${TARGET_OS}/Config_heavy.pl
}

# Create a perl-modules package recommending all the other perl
# packages (actually the non modules packages and not created too)
ALLOW_EMPTY_perl-modules = 1
PACKAGES_append = " perl-modules"
RRECOMMENDS_perl-modules = "${PACKAGES}"
RPROVIDES_perl-lib = "perl-lib"


include perl-rdepends_${PV}.inc

# To create/update the perl-rdepends_${PV}.inc use this piece of ugly script (modified for your arch/paths etc):
# daka@DaKa2:/home/slug/slugos/tmp/work/perl-5.8.7-r14/install$ egrep -r "use|require" * | grep ";$" | egrep ".pm:use |.pm:require " | grep -v v5.6.0 | grep -v 5.00 | grep -v \$module | sed -e "s, \+, ,g" | cut -f1,2 -d" " | sed -e "s,;, ,g" | sed -e "s,(), ,g" | sed -e "s,::,-,g" | sort | uniq | tr [:upper:] [:lower:] | sed -e "s,/[^ ]\+ , += \"perl-module-,g" | sed -e "s, \?$, \",g" | sed -e "s,_,-,g" | sed -e "s,^,RDEPENDS_,g" | sed -e "s,armeb-linux,\$\{TARGET_ARCH\}-\$\{TARGET_OS\},g" | egrep -v "perl-module-5|perl-module-tk|perl-module-mac-internetconfig|perl-module-ndbm-file|perl-module-html-treebuilder|perl-module-lwp-simple|perl-module-vms-filespec|perl-module-fcgi|perl-module-vms-stdio|perl-module-mac-buildtools" > /home/slug/openembedded/packages/perl/perl-rdepends_5.8.7.inc

# Some packages changed names in 5.8.7-r14, RPROVIDE them
RPROVIDES_perl-module-b-asmdata = "perl-module-${TARGET_SYS}-b-asmdata"
RPROVIDES_perl-module-b-assembler = "perl-module-${TARGET_SYS}-b-assembler"
RPROVIDES_perl-module-b-bblock = "perl-module-${TARGET_SYS}-b-bblock"
RPROVIDES_perl-module-b-bytecode = "perl-module-${TARGET_SYS}-b-bytecode"
RPROVIDES_perl-module-b-cc = "perl-module-${TARGET_SYS}-b-cc"
RPROVIDES_perl-module-b-concise = "perl-module-${TARGET_SYS}-b-concise"
RPROVIDES_perl-module-b-debug = "perl-module-${TARGET_SYS}-b-debug"
RPROVIDES_perl-module-b-deparse = "perl-module-${TARGET_SYS}-b-deparse"
RPROVIDES_perl-module-b-disassembler = "perl-module-${TARGET_SYS}-b-disassembler"
RPROVIDES_perl-module-b-lint = "perl-module-${TARGET_SYS}-b-lint"
RPROVIDES_perl-module-b-showlex = "perl-module-${TARGET_SYS}-b-showlex"
RPROVIDES_perl-module-b-stackobj = "perl-module-${TARGET_SYS}-b-stackobj"
RPROVIDES_perl-module-b-stash = "perl-module-${TARGET_SYS}-b-stash"
RPROVIDES_perl-module-b-terse = "perl-module-${TARGET_SYS}-b-terse"
RPROVIDES_perl-module-b-xref = "perl-module-${TARGET_SYS}-b-xref"
RPROVIDES_perl-module-config = "perl-module-${TARGET_SYS}-config"
RPROVIDES_perl-module-config-heavy = "perl-module-${TARGET_SYS}-config-heavy"
RPROVIDES_perl-module-encode-alias = "perl-module-${TARGET_SYS}-encode-alias"
RPROVIDES_perl-module-encode-cjkconstants = "perl-module-${TARGET_SYS}-encode-cjkconstants"
RPROVIDES_perl-module-encode-config = "perl-module-${TARGET_SYS}-encode-config"
RPROVIDES_perl-module-encode-encoder = "perl-module-${TARGET_SYS}-encode-encoder"
RPROVIDES_perl-module-encode-encoding = "perl-module-${TARGET_SYS}-encode-encoding"
RPROVIDES_perl-module-encode-guess = "perl-module-${TARGET_SYS}-encode-guess"
RPROVIDES_perl-module-encoding = "perl-module-${TARGET_SYS}-encoding"
RPROVIDES_perl-module-errno = "perl-module-${TARGET_SYS}-errno"
RPROVIDES_perl-module-io-dir = "perl-module-${TARGET_SYS}-io-dir"
RPROVIDES_perl-module-io-file = "perl-module-${TARGET_SYS}-io-file"
RPROVIDES_perl-module-io-handle = "perl-module-${TARGET_SYS}-io-handle"
RPROVIDES_perl-module-io-pipe = "perl-module-${TARGET_SYS}-io-pipe"
RPROVIDES_perl-module-io-poll = "perl-module-${TARGET_SYS}-io-poll"
RPROVIDES_perl-module-io-seekable = "perl-module-${TARGET_SYS}-io-seekable"
RPROVIDES_perl-module-io-select = "perl-module-${TARGET_SYS}-io-select"
RPROVIDES_perl-module-io-socket = "perl-module-${TARGET_SYS}-io-socket"
RPROVIDES_perl-module-ipc-msg = "perl-module-${TARGET_SYS}-ipc-msg"
RPROVIDES_perl-module-ipc-semaphore = "perl-module-${TARGET_SYS}-ipc-semaphore"
RPROVIDES_perl-module-lib = "perl-module-${TARGET_SYS}-lib"
RPROVIDES_perl-module-mime-quotedprint = "perl-module-${TARGET_SYS}-mime-quotedprint"
RPROVIDES_perl-module-o = "perl-module-${TARGET_SYS}-o"
RPROVIDES_perl-module-ops = "perl-module-${TARGET_SYS}-ops"
RPROVIDES_perl-module-safe = "perl-module-${TARGET_SYS}-safe"
RPROVIDES_perl-module-xsloader = "perl-module-${TARGET_SYS}-xsloader"
