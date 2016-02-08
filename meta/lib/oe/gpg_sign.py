"""Helper module for GPG signing"""
import os

import bb
import oe.utils

class LocalSigner(object):
    """Class for handling local (on the build host) signing"""
    def __init__(self, d, keyid, passphrase_file):
        self.keyid = keyid
        self.passphrase_file = passphrase_file
        self.gpg_bin = d.getVar('GPG_BIN', True) or \
                  bb.utils.which(os.getenv('PATH'), 'gpg')
        self.gpg_path = d.getVar('GPG_PATH', True)
        self.rpm_bin = bb.utils.which(os.getenv('PATH'), "rpm")

    def export_pubkey(self, output_file):
        """Export GPG public key to a file"""
        cmd = '%s --batch --yes --export --armor -o %s ' % \
                (self.gpg_bin, output_file)
        if self.gpg_path:
            cmd += "--homedir %s " % self.gpg_path
        cmd += self.keyid
        status, output = oe.utils.getstatusoutput(cmd)
        if status:
            raise bb.build.FuncFailed('Failed to export gpg public key (%s): %s' %
                                      (self.keyid, output))

    def sign_rpms(self, files):
        """Sign RPM files"""
        import pexpect

        cmd = self.rpm_bin + " --addsign --define '_gpg_name %s' " % self.keyid
        if self.gpg_bin:
            cmd += "--define '%%__gpg %s' " % self.gpg_bin
        if self.gpg_path:
            cmd += "--define '_gpg_path %s' " % self.gpg_path
        cmd += ' '.join(files)

        # Need to use pexpect for feeding the passphrase
        proc = pexpect.spawn(cmd)
        try:
            proc.expect_exact('Enter pass phrase:', timeout=15)
            with open(self.passphrase_file) as fobj:
                proc.sendline(fobj.readline().rstrip('\n'))
            proc.expect(pexpect.EOF, timeout=900)
            proc.close()
        except pexpect.TIMEOUT as err:
            bb.error('rpmsign timeout: %s' % err)
            proc.terminate()
        if os.WEXITSTATUS(proc.status) or not os.WIFEXITED(proc.status):
            bb.error('rpmsign failed: %s' % proc.before.strip())
            raise bb.build.FuncFailed("Failed to sign RPM packages")

    def detach_sign(self, input_file):
        """Create a detached signature of a file"""
        cmd = "%s --detach-sign --armor --batch --no-tty --yes " \
                  "--passphrase-file '%s' -u '%s' " % \
                  (self.gpg_bin, self.passphrase_file, self.keyid)
        if self.gpg_path:
            cmd += "--homedir %s " % self.gpg_path
        cmd += input_file
        status, output = oe.utils.getstatusoutput(cmd)
        if status:
            raise bb.build.FuncFailed("Failed to create signature for '%s': %s" %
                                      (input_file, output))


def get_signer(d, backend, keyid, passphrase_file):
    """Get signer object for the specified backend"""
    # Use local signing by default
    if backend == 'local':
        return LocalSigner(d, keyid, passphrase_file)
    else:
        bb.fatal("Unsupported signing backend '%s'" % backend)

