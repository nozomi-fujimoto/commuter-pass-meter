type StatusMessageProps = {
  loading: boolean;
  error: string;
  notice: string;
};

export function StatusMessage({ loading, error, notice }: StatusMessageProps) {
  if (loading) {
    return <p className="statusMessage">読み込み中</p>;
  }
  if (error) {
    return <p className="statusMessage error">{error}</p>;
  }
  if (notice) {
    return <p className="statusMessage success">{notice}</p>;
  }
  return null;
}
