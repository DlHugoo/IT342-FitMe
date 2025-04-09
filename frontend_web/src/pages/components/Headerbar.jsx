import UserWhiteIcon from "../../assets/user-white.png";

const Headerbar = () => {
  return (
    <>
      <header className="bg-blue-header text-white p-4 relative flex items-center justify-end">
        {/* Centered Title */}
        <h1 className="absolute left-1/2 transform -translate-x-1/2 text-2xl font-semibold">
          Admin Management
        </h1>

        {/* Right-aligned Admin Info */}
        <div className="flex items-center">
          <img src={UserWhiteIcon} alt="User" className="h-4 w-4 mr-2" />
          <span className="mr-2">admin</span>
          <div className="text-white text-[10px]">▼</div>
        </div>
      </header>
    </>
  );
};

export default Headerbar;
