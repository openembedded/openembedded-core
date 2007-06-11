DESCRIPTION = "Twisted is an event-driven networking framework written in Python and licensed under the LGPL. \
Twisted supports TCP, UDP, SSL/TLS, multicast, Unix sockets, a large number of protocols                   \
(including HTTP, NNTP, IMAP, SSH, IRC, FTP, and others), and much more."
HOMEPAGE = "http://www.twistedmatrix.com"
SECTION = "console/network"
PRIORITY = "optional"
LICENSE = "LGPL"
RDEPENDS = "python-core python-zopeinterface"
RDEPENDS_python-twisted += "python-twisted-bin python-twisted-conch python-twisted-core \
                            python-twisted-lore python-twisted-mail python-twisted-names \
                            python-twisted-news python-twisted-runner python-twisted-web \
                            python-twisted-words"
PR = "r5"

SRC_URI = "http://tmrc.mit.edu/mirror/twisted/Twisted/2.5/Twisted-${PV}.tar.bz2 \
           file://remove-zope-check.patch;patch=1"

S = "${WORKDIR}/Twisted-${PV}"

inherit distutils

PACKAGES += "python-twisted-zsh python-twisted-test python-twisted-protocols \
             python-twisted-bin  python-twisted-conch python-twisted-lore \
             python-twisted-mail python-twisted-names python-twisted-news python-twisted-runner \
             python-twisted-web  python-twisted-words python-twisted python-twisted-core \
             "

ALLOW_EMPTY = "1"
FILES_${PN} = ""
FILES_python-twisted = ""

FILES_python-twisted-test = " \
${libdir}/python2.4/site-packages/twisted/python/web/test \
"

FILES_python-twisted-protocols = " \
${libdir}/python2.4/site-packages/twisted/python/protocols/ \
"

FILES_python-twisted-zsh = " \
${libdir}/python2.4/site-packages/twisted/python/zsh \
${libdir}/python2.4/site-packages/twisted/python/zshcomp.* \
"

FILES_python-twisted-bin = " \
${libdir}/python2.4/site-packages/twisted/protocols/_c_urlarg.so \
${libdir}/python2.4/site-packages/twisted/spread/cBanana.so"

FILES_python-twisted-conch = " \
${bindir}/ckeygen \
${bindir}/tkconch \
${bindir}/conch \
${bindir}/conchftp \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_conch.py \
${libdir}/python2.4/site-packages/twisted/conch  \
"

FILES_python-twisted-core = " \
${bindir}/manhole \
${bindir}/mktap \
${bindir}/twistd \
${bindir}/tap2deb \
${bindir}/tap2rpm \
${bindir}/tapconvert \
${bindir}/tkmktap \
${bindir}/trial \
${libdir}/python2.4/site-packages/twisted/*.py \
${libdir}/python2.4/site-packages/twisted/plugins/__init__.py \
${libdir}/python2.4/site-packages/twisted/plugins/notestplugin.py \
${libdir}/python2.4/site-packages/twisted/plugins/testplugin.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_ftp.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_inet.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_manhole.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_portforward.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_socks.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_telnet.py \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_trial.py \
${libdir}/python2.4/site-packages/twisted/plugins/dropin.cache \
${libdir}/python2.4/site-packages/twisted/application \
${libdir}/python2.4/site-packages/twisted/cred \
${libdir}/python2.4/site-packages/twisted/enterprise \
${libdir}/python2.4/site-packages/twisted/internet \
${libdir}/python2.4/site-packages/twisted/manhole \
${libdir}/python2.4/site-packages/twisted/manhole \
${libdir}/python2.4/site-packages/twisted/persisted \
${libdir}/python2.4/site-packages/twisted/protocols\
${libdir}/python2.4/site-packages/twisted/python\
${libdir}/python2.4/site-packages/twisted/python/timeoutqueue.py \
${libdir}/python2.4/site-packages/twisted/python/filepath.py \
${libdir}/python2.4/site-packages/twisted/python/dxprofile.py \
${libdir}/python2.4/site-packages/twisted/python/plugin.py \
${libdir}/python2.4/site-packages/twisted/python/htmlizer.py \
${libdir}/python2.4/site-packages/twisted/python/__init__.py \
${libdir}/python2.4/site-packages/twisted/python/dispatch.py \
${libdir}/python2.4/site-packages/twisted/python/hook.py \
${libdir}/python2.4/site-packages/twisted/python/threadpool.py \
${libdir}/python2.4/site-packages/twisted/python/otp.py \
${libdir}/python2.4/site-packages/twisted/python/usage.py \
${libdir}/python2.4/site-packages/twisted/python/roots.py \
${libdir}/python2.4/site-packages/twisted/python/versions.py \
${libdir}/python2.4/site-packages/twisted/python/urlpath.py \
${libdir}/python2.4/site-packages/twisted/python/util.py \
${libdir}/python2.4/site-packages/twisted/python/components.py \
${libdir}/python2.4/site-packages/twisted/python/logfile.py \
${libdir}/python2.4/site-packages/twisted/python/runtime.py \
${libdir}/python2.4/site-packages/twisted/python/reflect.py \
${libdir}/python2.4/site-packages/twisted/python/context.py \
${libdir}/python2.4/site-packages/twisted/python/threadable.py \
${libdir}/python2.4/site-packages/twisted/python/rebuild.py \
${libdir}/python2.4/site-packages/twisted/python/failure.py \
${libdir}/python2.4/site-packages/twisted/python/lockfile.py \
${libdir}/python2.4/site-packages/twisted/python/formmethod.py \
${libdir}/python2.4/site-packages/twisted/python/finalize.py \
${libdir}/python2.4/site-packages/twisted/python/win32.py \
${libdir}/python2.4/site-packages/twisted/python/dist.py \
${libdir}/python2.4/site-packages/twisted/python/shortcut.py \
${libdir}/python2.4/site-packages/twisted/python/zipstream.py \
${libdir}/python2.4/site-packages/twisted/python/release.py \
${libdir}/python2.4/site-packages/twisted/python/syslog.py \
${libdir}/python2.4/site-packages/twisted/python/log.py \
${libdir}/python2.4/site-packages/twisted/python/compat.py \
${libdir}/python2.4/site-packages/twisted/python/zshcomp.py \
${libdir}/python2.4/site-packages/twisted/python/procutils.py \
${libdir}/python2.4/site-packages/twisted/python/text.py \
${libdir}/python2.4/site-packages/twisted/python/_twisted_zsh_stub \
${libdir}/python2.4/site-packages/twisted/scripts/ \
${libdir}/python2.4/site-packages/twisted/spread/ \
${libdir}/python2.4/site-packages/twisted/tap/ \
${libdir}/python2.4/site-packages/twisted/trial/ \
${libdir}/python2.4/site-packages/twisted/__init__.py \
${libdir}/python2.4/site-packages/twisted/_version.py \
${libdir}/python2.4/site-packages/twisted/copyright.py \
${libdir}/python2.4/site-packages/twisted/im.py \
${libdir}/python2.4/site-packages/twisted/plugin.py \
"

FILES_python-twisted-lore = " \
${bindir}/bookify \
${bindir}/lore \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_lore.py \
${libdir}/python2.4/site-packages/twisted/lore \
"

FILES_python-twisted-mail = " \
${bindir}/mailmail \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_mail.py \
${libdir}/python2.4/site-packages/twisted/mail \
"

FILES_python-twisted-names = " \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_names.py \
${libdir}/python2.4/site-packages/twisted/names \
"

FILES_python-twisted-news = " \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_news.py \
${libdir}/python2.4/site-packages/twisted/news \
"

FILES_python-twisted-runner = " \
${libdir}python2.4/site-packages/twisted/runner/portmap.so \
${libdir}/python2.4/site-packages/twisted/runner\
"

FILES_python-twisted-web = " \
${bindir}/websetroot \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_web.py \
${libdir}/python2.4/site-packages/twisted/web\
"

FILES_python-twisted-words = " \
${bindir}/im \
${libdir}/python2.4/site-packages/twisted/plugins/twisted_words.py \
${libdir}/python2.4/site-packages/twisted/words\
"
