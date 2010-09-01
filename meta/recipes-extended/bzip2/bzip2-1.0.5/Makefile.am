
lib_LTLIBRARIES = libbz2.la

libbz2_la_SOURCES = blocksort.c  \
                    huffman.c    \
                    crctable.c   \
                    randtable.c  \
                    compress.c   \
                    decompress.c \
                    bzlib.c

bin_PROGRAMS = bzip2 bzip2recover

bzip2_SOURCES = bzip2.c
bzip2_LDADD = libbz2.la
bzip2_DEPENDENCIES = libbz2.la

include_HEADERS = bzlib.h

bzip2recover_SOURCES = bzip2recover.c
bzip2recover_LDADD = libbz2.la
bzip2recover_DEPENDENCIES = libbz2.la

bin_SCRIPTS = bzgrep bzmore bzdiff

man_MANS = bzip2.1 bzgrep.1 bzmore.1 bzdiff.1
EXTRA_DIST = $(man_MANS)

install-exec-hook:
	ln -s $(bindir)/bzip2$(EXEEXT) $(DESTDIR)$(bindir)/bunzip2$(EXEEXT)
	ln -s $(bindir)/bzip2$(EXEEXT) $(DESTDIR)$(bindir)/bzcat$(EXEEXT)
	ln -s $(bindir)/bzgrep$(EXEEXT) $(DESTDIR)$(bindir)/bzegrep$(EXEEXT)
	ln -s $(bindir)/bzgrep$(EXEEXT) $(DESTDIR)$(bindir)/bzfgrep$(EXEEXT)
	ln -s $(bindir)/bzmore$(EXEEXT) $(DESTDIR)$(bindir)/bzless$(EXEEXT)
	ln -s $(bindir)/bzdiff$(EXEEXT) $(DESTDIR)$(bindir)/bzcmp$(EXEEXT)

install-data-hook:
	echo ".so man1/bzgrep.1" > $(DESTDIR)$(mandir)/man1/bzegrep.1
	echo ".so man1/bzgrep.1" > $(DESTDIR)$(mandir)/man1/bzfgrep.1
	echo ".so man1/bzmore.1" > $(DESTDIR)$(mandir)/man1/bzless.1
	echo ".so man1/bzdiff.1" > $(DESTDIR)$(mandir)/man1/bzcmp.1
