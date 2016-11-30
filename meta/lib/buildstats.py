# Implements system state sampling. Called by buildstats.bbclass.
# Because it is a real Python module, it can hold persistent state,
# like open log files and the time of the last sampling.

import time

class SystemStats:
    def __init__(self, d):
        bn = d.getVar('BUILDNAME', True)
        bsdir = os.path.join(d.getVar('BUILDSTATS_BASE', True), bn)
        bb.utils.mkdirhier(bsdir)

        self.proc_files = []
        for filename in ('diskstats', 'meminfo', 'stat'):
            # In practice, this class gets instantiated only once in
            # the bitbake cooker process.  Therefore 'append' mode is
            # not strictly necessary, but using it makes the class
            # more robust should two processes ever write
            # concurrently.
            self.proc_files.append((filename,
                                    open(os.path.join(bsdir, 'proc_%s.log' % filename), 'ab')))
        # Last time that we sampled data.
        self.last = 0
        # Minimum number of seconds between recording a sample. This
        # becames relevant when we get called very often while many
        # short tasks get started. Sampling during quiet periods
        # depends on the heartbeat event, which fires less often.
        self.min_seconds = 1

    def close(self):
        self.monitor_disk.close()
        for _, output, _ in self.proc_files:
            output.close()

    def sample(self, force):
        now = time.time()
        if (now - self.last > self.min_seconds) or force:
            for filename, output in self.proc_files:
                with open(os.path.join('/proc', filename), 'rb') as input:
                    data = input.read()
                    # Unbuffered raw write, less overhead and useful
                    # in case that we end up with concurrent writes.
                    os.write(output.fileno(),
                             ('%.0f\n' % now).encode('ascii') +
                             data +
                             b'\n')
            self.last = now
